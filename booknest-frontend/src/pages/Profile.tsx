import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useMutation, useQueryClient } from 'react-query';
import { toast } from 'react-hot-toast';
import { User, Mail, Phone, MapPin, Edit, Save, X } from 'lucide-react';
import { useAuth } from '../contexts/AuthContext';
import apiService from '../services/api';
import { User as UserType } from '../types';

interface ProfileFormData {
  firstName: string;
  lastName: string;
  email: string;
  phone?: string;
  address?: string;
}

const Profile: React.FC = () => {
  const { user, updateUserProfile } = useAuth();
  const queryClient = useQueryClient();
  const [isEditing, setIsEditing] = useState(false);

  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm<ProfileFormData>({
    defaultValues: {
      firstName: user?.firstName || '',
      lastName: user?.lastName || '',
      email: user?.email || '',
      phone: user?.phone || '',
      address: user?.address || '',
    },
  });

  const updateProfileMutation = useMutation(
    (data: Partial<UserType>) => apiService.updateProfile(data),
    {
      onSuccess: (updatedUser) => {
        updateUserProfile(updatedUser);
        toast.success('Profile updated successfully!');
        setIsEditing(false);
        queryClient.invalidateQueries('user');
      },
      onError: () => {
        toast.error('Failed to update profile');
      },
    }
  );

  const onSubmit = (data: ProfileFormData) => {
    updateProfileMutation.mutate(data);
  };

  const handleCancel = () => {
    reset();
    setIsEditing(false);
  };

  if (!user) {
    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="text-center">
          <h2 className="text-2xl font-bold text-gray-900 mb-4">User not found</h2>
          <p className="text-gray-600">Please log in to view your profile.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900">My Profile</h1>
        <p className="text-gray-600 mt-2">
          Manage your account information and preferences
        </p>
      </div>

      <div className="bg-white rounded-lg shadow-sm border border-gray-200">
        <div className="card-header">
          <div className="flex justify-between items-center">
            <h2 className="text-xl font-semibold text-gray-900">Personal Information</h2>
            {!isEditing ? (
              <button
                onClick={() => setIsEditing(true)}
                className="btn btn-secondary flex items-center space-x-2"
              >
                <Edit className="h-4 w-4" />
                <span>Edit Profile</span>
              </button>
            ) : (
              <div className="flex space-x-2">
                <button
                  onClick={handleSubmit(onSubmit)}
                  disabled={updateProfileMutation.isLoading}
                  className="btn btn-primary flex items-center space-x-2"
                >
                  <Save className="h-4 w-4" />
                  <span>
                    {updateProfileMutation.isLoading ? 'Saving...' : 'Save Changes'}
                  </span>
                </button>
                <button
                  onClick={handleCancel}
                  className="btn btn-secondary flex items-center space-x-2"
                >
                  <X className="h-4 w-4" />
                  <span>Cancel</span>
                </button>
              </div>
            )}
          </div>
        </div>

        <form onSubmit={handleSubmit(onSubmit)}>
          <div className="card-body space-y-6">
            {/* Profile Picture */}
            <div className="flex items-center space-x-6">
              <div className="w-20 h-20 bg-primary-100 rounded-full flex items-center justify-center">
                <User className="h-10 w-10 text-primary-600" />
              </div>
              <div>
                <h3 className="text-lg font-medium text-gray-900">
                  {user.firstName} {user.lastName}
                </h3>
                <p className="text-gray-600">{user.role}</p>
              </div>
            </div>

            {/* Name Fields */}
            <div className="grid md:grid-cols-2 gap-6">
              <div>
                <label htmlFor="firstName" className="block text-sm font-medium text-gray-700 mb-2">
                  First Name
                </label>
                <input
                  id="firstName"
                  type="text"
                  disabled={!isEditing}
                  className={`input ${!isEditing ? 'bg-gray-50' : ''} ${errors.firstName ? 'border-red-500' : ''}`}
                  {...register('firstName', {
                    required: 'First name is required',
                    minLength: {
                      value: 2,
                      message: 'First name must be at least 2 characters',
                    },
                  })}
                />
                {errors.firstName && (
                  <p className="mt-1 text-sm text-red-600">{errors.firstName.message}</p>
                )}
              </div>

              <div>
                <label htmlFor="lastName" className="block text-sm font-medium text-gray-700 mb-2">
                  Last Name
                </label>
                <input
                  id="lastName"
                  type="text"
                  disabled={!isEditing}
                  className={`input ${!isEditing ? 'bg-gray-50' : ''} ${errors.lastName ? 'border-red-500' : ''}`}
                  {...register('lastName', {
                    required: 'Last name is required',
                    minLength: {
                      value: 2,
                      message: 'Last name must be at least 2 characters',
                    },
                  })}
                />
                {errors.lastName && (
                  <p className="mt-1 text-sm text-red-600">{errors.lastName.message}</p>
                )}
              </div>
            </div>

            {/* Email */}
            <div>
              <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-2">
                Email Address
              </label>
              <div className="relative">
                <input
                  id="email"
                  type="email"
                  disabled={!isEditing}
                  className={`input pl-10 ${!isEditing ? 'bg-gray-50' : ''} ${errors.email ? 'border-red-500' : ''}`}
                  {...register('email', {
                    required: 'Email is required',
                    pattern: {
                      value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                      message: 'Invalid email address',
                    },
                  })}
                />
                <Mail className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400" />
              </div>
              {errors.email && (
                <p className="mt-1 text-sm text-red-600">{errors.email.message}</p>
              )}
            </div>

            {/* Phone */}
            <div>
              <label htmlFor="phone" className="block text-sm font-medium text-gray-700 mb-2">
                Phone Number
              </label>
              <div className="relative">
                <input
                  id="phone"
                  type="tel"
                  disabled={!isEditing}
                  className={`input pl-10 ${!isEditing ? 'bg-gray-50' : ''}`}
                  placeholder="Enter your phone number"
                  {...register('phone')}
                />
                <Phone className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400" />
              </div>
            </div>

            {/* Address */}
            <div>
              <label htmlFor="address" className="block text-sm font-medium text-gray-700 mb-2">
                Address
              </label>
              <div className="relative">
                <textarea
                  id="address"
                  rows={3}
                  disabled={!isEditing}
                  className={`input pl-10 resize-none ${!isEditing ? 'bg-gray-50' : ''}`}
                  placeholder="Enter your address"
                  {...register('address')}
                />
                <MapPin className="absolute left-3 top-3 h-5 w-5 text-gray-400" />
              </div>
            </div>

            {/* Account Info */}
            <div className="border-t pt-6">
              <h3 className="text-lg font-medium text-gray-900 mb-4">Account Information</h3>
              <div className="grid md:grid-cols-2 gap-6">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Account Type
                  </label>
                  <div className="input bg-gray-50">
                    <span className="text-gray-900">{user.role}</span>
                  </div>
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Member Since
                  </label>
                  <div className="input bg-gray-50">
                    <span className="text-gray-900">
                      {new Date(user.createdAt).toLocaleDateString()}
                    </span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </form>
      </div>

      {/* Additional Sections */}
      <div className="mt-8 grid md:grid-cols-2 gap-6">
        {/* Recent Orders */}
        <div className="bg-white rounded-lg shadow-sm border border-gray-200">
          <div className="card-header">
            <h3 className="text-lg font-semibold text-gray-900">Recent Orders</h3>
          </div>
          <div className="card-body">
            <p className="text-gray-600 text-sm">
              View your recent order history and track current orders.
            </p>
            <div className="mt-4">
              <a
                href="/orders"
                className="text-primary-600 hover:text-primary-700 font-medium text-sm"
              >
                View All Orders →
              </a>
            </div>
          </div>
        </div>

        {/* Account Settings */}
        <div className="bg-white rounded-lg shadow-sm border border-gray-200">
          <div className="card-header">
            <h3 className="text-lg font-semibold text-gray-900">Account Settings</h3>
          </div>
          <div className="card-body">
            <p className="text-gray-600 text-sm">
              Manage your account settings and preferences.
            </p>
            <div className="mt-4 space-y-2">
              <button className="block w-full text-left text-primary-600 hover:text-primary-700 font-medium text-sm">
                Change Password
              </button>
              <button className="block w-full text-left text-primary-600 hover:text-primary-700 font-medium text-sm">
                Notification Settings
              </button>
              <button className="block w-full text-left text-red-600 hover:text-red-700 font-medium text-sm">
                Delete Account
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profile;





