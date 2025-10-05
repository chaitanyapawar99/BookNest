import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { useMutation, useQuery, useQueryClient } from 'react-query';
import { toast } from 'react-hot-toast';
import { Upload, BookOpen, DollarSign, FileText, Image } from 'lucide-react';
import apiService from '../services/api';
import { BookDTO, Category } from '../types';

interface AddBookFormData {
  title: string;
  author: string;
  description: string;
  price: number;
  categoryId: number;
}

const AddBook: React.FC = () => {
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const [imageFile, setImageFile] = useState<File | null>(null);
  const [documentFile, setDocumentFile] = useState<File | null>(null);
  const [imagePreview, setImagePreview] = useState<string>('');

  // Fetch categories
  const { data: categories = [], isLoading: categoriesLoading } = useQuery(
    'categories',
    () => apiService.getCategories()
  );

  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm<AddBookFormData>();

  const createBookMutation = useMutation(
    (bookData: BookDTO) => apiService.createBook(bookData),
    {
      onSuccess: () => {
        toast.success('Book added successfully! It will be reviewed by our team.');
        queryClient.invalidateQueries('books');
        reset();
        setImageFile(null);
        setDocumentFile(null);
        setImagePreview('');
        navigate('/books');
      },
      onError: () => {
        toast.error('Failed to add book. Please try again.');
      },
    }
  );

  const uploadFileMutation = useMutation(
    (file: File) => apiService.uploadFile(file, 'image'),
    {
      onError: () => {
        toast.error('Failed to upload image');
      },
    }
  );

  const uploadDocumentMutation = useMutation(
    (file: File) => apiService.uploadFile(file, 'document'),
    {
      onError: () => {
        toast.error('Failed to upload document');
      },
    }
  );

  const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      setImageFile(file);
      const reader = new FileReader();
      reader.onloadend = () => {
        setImagePreview(reader.result as string);
      };
      reader.readAsDataURL(file);
    }
  };

  const handleDocumentChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      setDocumentFile(file);
    }
  };

  const onSubmit = async (data: AddBookFormData) => {
    try {
      let imagePath = '';
      let filePath = '';

      // Upload image if selected
      if (imageFile) {
        imagePath = await uploadFileMutation.mutateAsync(imageFile);
      }

      // Upload document if selected
      if (documentFile) {
        filePath = await uploadDocumentMutation.mutateAsync(documentFile);
      }

      const bookData: BookDTO = {
        ...data,
        imagePath: imagePath || undefined,
        filePath: filePath || undefined,
        approved: false,
        available: true,
      };

      createBookMutation.mutate(bookData);
    } catch (error) {
      toast.error('Failed to upload files');
    }
  };

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900">Add New Book</h1>
        <p className="text-gray-600 mt-2">
          Share your books with the BookNest community
        </p>
      </div>

      <div className="bg-white rounded-lg shadow-sm border border-gray-200">
        <div className="card-header">
          <h2 className="text-xl font-semibold text-gray-900">Book Information</h2>
        </div>

        <form onSubmit={handleSubmit(onSubmit)}>
          <div className="card-body space-y-6">
            {/* Basic Information */}
            <div>
              <h3 className="text-lg font-medium text-gray-900 mb-4">Basic Information</h3>
              <div className="grid md:grid-cols-2 gap-6">
                <div>
                  <label htmlFor="title" className="block text-sm font-medium text-gray-700 mb-2">
                    Book Title *
                  </label>
                  <input
                    id="title"
                    type="text"
                    className={`input ${errors.title ? 'border-red-500' : ''}`}
                    placeholder="Enter book title"
                    {...register('title', {
                      required: 'Book title is required',
                      minLength: {
                        value: 2,
                        message: 'Title must be at least 2 characters',
                      },
                    })}
                  />
                  {errors.title && (
                    <p className="mt-1 text-sm text-red-600">{errors.title.message}</p>
                  )}
                </div>

                <div>
                  <label htmlFor="author" className="block text-sm font-medium text-gray-700 mb-2">
                    Author *
                  </label>
                  <input
                    id="author"
                    type="text"
                    className={`input ${errors.author ? 'border-red-500' : ''}`}
                    placeholder="Enter author name"
                    {...register('author', {
                      required: 'Author is required',
                      minLength: {
                        value: 2,
                        message: 'Author name must be at least 2 characters',
                      },
                    })}
                  />
                  {errors.author && (
                    <p className="mt-1 text-sm text-red-600">{errors.author.message}</p>
                  )}
                </div>
              </div>
            </div>

            {/* Category and Price */}
            <div className="grid md:grid-cols-2 gap-6">
              <div>
                <label htmlFor="categoryId" className="block text-sm font-medium text-gray-700 mb-2">
                  Category *
                </label>
                <select
                  id="categoryId"
                  className={`input ${errors.categoryId ? 'border-red-500' : ''}`}
                  {...register('categoryId', {
                    required: 'Category is required',
                  })}
                >
                  <option value="">Select a category</option>
                  {categories.map((category: Category) => (
                    <option key={category.id} value={category.id}>
                      {category.name}
                    </option>
                  ))}
                </select>
                {errors.categoryId && (
                  <p className="mt-1 text-sm text-red-600">{errors.categoryId.message}</p>
                )}
              </div>

              <div>
                <label htmlFor="price" className="block text-sm font-medium text-gray-700 mb-2">
                  Price *
                </label>
                <div className="relative">
                  <input
                    id="price"
                    type="number"
                    step="0.01"
                    min="0"
                    className={`input pl-10 ${errors.price ? 'border-red-500' : ''}`}
                    placeholder="0.00"
                    {...register('price', {
                      required: 'Price is required',
                      min: {
                        value: 0,
                        message: 'Price must be positive',
                      },
                    })}
                  />
                  <DollarSign className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400" />
                </div>
                {errors.price && (
                  <p className="mt-1 text-sm text-red-600">{errors.price.message}</p>
                )}
              </div>
            </div>

            {/* Description */}
            <div>
              <label htmlFor="description" className="block text-sm font-medium text-gray-700 mb-2">
                Description
              </label>
              <textarea
                id="description"
                rows={4}
                className={`input resize-none ${errors.description ? 'border-red-500' : ''}`}
                placeholder="Enter book description..."
                {...register('description', {
                  maxLength: {
                    value: 1000,
                    message: 'Description must be less than 1000 characters',
                  },
                })}
              />
              {errors.description && (
                <p className="mt-1 text-sm text-red-600">{errors.description.message}</p>
              )}
            </div>

            {/* File Uploads */}
            <div>
              <h3 className="text-lg font-medium text-gray-900 mb-4">Book Files</h3>
              
              {/* Image Upload */}
              <div className="mb-6">
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Book Cover Image
                </label>
                <div className="flex items-center space-x-4">
                  <div className="flex-1">
                    <input
                      type="file"
                      accept="image/*"
                      onChange={handleImageChange}
                      className="hidden"
                      id="image-upload"
                    />
                    <label
                      htmlFor="image-upload"
                      className="cursor-pointer flex items-center justify-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500"
                    >
                      <Image className="h-5 w-5 mr-2" />
                      Choose Image
                    </label>
                  </div>
                  {imageFile && (
                    <span className="text-sm text-gray-600">
                      {imageFile.name}
                    </span>
                  )}
                </div>
                {imagePreview && (
                  <div className="mt-4">
                    <img
                      src={imagePreview}
                      alt="Preview"
                      className="w-32 h-40 object-cover rounded border"
                    />
                  </div>
                )}
              </div>

              {/* Document Upload */}
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Book File (PDF, EPUB, etc.)
                </label>
                <div className="flex items-center space-x-4">
                  <div className="flex-1">
                    <input
                      type="file"
                      accept=".pdf,.epub,.mobi,.txt"
                      onChange={handleDocumentChange}
                      className="hidden"
                      id="document-upload"
                    />
                    <label
                      htmlFor="document-upload"
                      className="cursor-pointer flex items-center justify-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500"
                    >
                      <FileText className="h-5 w-5 mr-2" />
                      Choose File
                    </label>
                  </div>
                  {documentFile && (
                    <span className="text-sm text-gray-600">
                      {documentFile.name}
                    </span>
                  )}
                </div>
                <p className="mt-2 text-sm text-gray-500">
                  Supported formats: PDF, EPUB, MOBI, TXT (Max 50MB)
                </p>
              </div>
            </div>

            {/* Guidelines */}
            <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
              <h4 className="text-sm font-medium text-blue-900 mb-2">Important Guidelines</h4>
              <ul className="text-sm text-blue-800 space-y-1">
                <li>• All books will be reviewed before being published</li>
                <li>• Ensure you have the rights to sell the book</li>
                <li>• Provide accurate book information</li>
                <li>• High-quality cover images are recommended</li>
                <li>• Books should be in readable format</li>
              </ul>
            </div>
          </div>

          <div className="card-footer">
            <div className="flex justify-end space-x-4">
              <button
                type="button"
                onClick={() => navigate('/books')}
                className="btn btn-secondary"
              >
                Cancel
              </button>
              <button
                type="submit"
                disabled={createBookMutation.isLoading}
                className="btn btn-primary flex items-center space-x-2"
              >
                {createBookMutation.isLoading ? (
                  <>
                    <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white"></div>
                    <span>Adding Book...</span>
                  </>
                ) : (
                  <>
                    <BookOpen className="h-4 w-4" />
                    <span>Add Book</span>
                  </>
                )}
              </button>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AddBook;





