import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from 'react-query';
import { toast } from 'react-hot-toast';
import { Star, ShoppingCart, Heart, Share2, ArrowLeft } from 'lucide-react';
import { useAuth } from '../contexts/AuthContext';
import apiService from '../services/api';
import { Book, Review } from '../types';

const BookDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { user } = useAuth();
  const queryClient = useQueryClient();
  const [activeTab, setActiveTab] = useState<'description' | 'reviews'>('description');

  // Fetch book details
  const { data: book, isLoading: bookLoading } = useQuery(
    ['book', id],
    () => apiService.getBook(Number(id)),
    {
      enabled: !!id,
    }
  );

  // Fetch book reviews
  const { data: reviews = [], isLoading: reviewsLoading } = useQuery(
    ['book-reviews', id],
    () => apiService.getBookReviews(Number(id)),
    {
      enabled: !!id,
    }
  );

  // Add to cart mutation
  const addToCartMutation = useMutation(
    (bookId: number) => apiService.addToCart(bookId),
    {
      onSuccess: () => {
        toast.success('Book added to cart!');
        queryClient.invalidateQueries('cart');
      },
      onError: () => {
        toast.error('Failed to add book to cart');
      },
    }
  );

  const handleAddToCart = () => {
    if (!user) {
      toast.error('Please login to add items to cart');
      navigate('/login');
      return;
    }
    addToCartMutation.mutate(Number(id));
  };

  if (bookLoading) {
    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="animate-pulse">
          <div className="flex items-center mb-6">
            <div className="h-6 w-6 bg-gray-200 rounded mr-2"></div>
            <div className="h-6 w-32 bg-gray-200 rounded"></div>
          </div>
          <div className="grid md:grid-cols-2 gap-8">
            <div className="h-96 bg-gray-200 rounded-lg"></div>
            <div className="space-y-4">
              <div className="h-8 bg-gray-200 rounded w-3/4"></div>
              <div className="h-6 bg-gray-200 rounded w-1/2"></div>
              <div className="h-4 bg-gray-200 rounded w-full"></div>
              <div className="h-4 bg-gray-200 rounded w-2/3"></div>
            </div>
          </div>
        </div>
      </div>
    );
  }

  if (!book) {
    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="text-center">
          <h2 className="text-2xl font-bold text-gray-900 mb-4">Book not found</h2>
          <p className="text-gray-600 mb-6">The book you're looking for doesn't exist.</p>
          <button
            onClick={() => navigate('/books')}
            className="btn btn-primary"
          >
            Browse Books
          </button>
        </div>
      </div>
    );
  }

  const averageRating = reviews.length > 0
    ? reviews.reduce((sum, review) => sum + review.rating, 0) / reviews.length
    : 0;

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      {/* Back Button */}
      <button
        onClick={() => navigate(-1)}
        className="flex items-center text-gray-600 hover:text-gray-900 mb-6"
      >
        <ArrowLeft className="h-4 w-4 mr-2" />
        Back
      </button>

      {/* Book Details */}
      <div className="grid md:grid-cols-2 gap-8 mb-12">
        {/* Book Image */}
        <div className="relative">
          <img
            src={book.imagePath || '/placeholder-book.jpg'}
            alt={book.title}
            className="w-full h-96 object-cover rounded-lg shadow-lg"
            onError={(e) => {
              const target = e.target as HTMLImageElement;
              target.src = '/placeholder-book.jpg';
            }}
          />
          <div className="absolute top-4 right-4 flex space-x-2">
            <button className="p-2 bg-white rounded-full shadow-md hover:bg-gray-50">
              <Heart className="h-5 w-5 text-gray-600" />
            </button>
            <button className="p-2 bg-white rounded-full shadow-md hover:bg-gray-50">
              <Share2 className="h-5 w-5 text-gray-600" />
            </button>
          </div>
        </div>

        {/* Book Info */}
        <div className="space-y-6">
          <div>
            <h1 className="text-3xl font-bold text-gray-900 mb-2">{book.title}</h1>
            <p className="text-xl text-gray-600 mb-4">by {book.author}</p>
            
            {/* Rating */}
            <div className="flex items-center space-x-2 mb-4">
              <div className="flex items-center">
                {[...Array(5)].map((_, index) => (
                  <Star
                    key={index}
                    className={`h-5 w-5 ${
                      index < Math.round(averageRating)
                        ? 'text-yellow-400 fill-current'
                        : 'text-gray-300'
                    }`}
                  />
                ))}
              </div>
              <span className="text-gray-600">
                {averageRating.toFixed(1)} ({reviews.length} reviews)
              </span>
            </div>

            {/* Category */}
            {book.category && (
              <div className="inline-block bg-primary-100 text-primary-800 px-3 py-1 rounded-full text-sm font-medium mb-4">
                {book.category.name}
              </div>
            )}
          </div>

          {/* Price */}
          <div className="text-3xl font-bold text-primary-600">
            ${book.price}
          </div>

          {/* Availability */}
          <div className="flex items-center space-x-2">
            <div className={`w-3 h-3 rounded-full ${book.available ? 'bg-green-500' : 'bg-red-500'}`}></div>
            <span className={book.available ? 'text-green-600' : 'text-red-600'}>
              {book.available ? 'In Stock' : 'Out of Stock'}
            </span>
          </div>

          {/* Action Buttons */}
          <div className="flex space-x-4">
            <button
              onClick={handleAddToCart}
              disabled={!book.available || addToCartMutation.isLoading}
              className="flex-1 btn btn-primary flex items-center justify-center space-x-2"
            >
              <ShoppingCart className="h-5 w-5" />
              <span>
                {addToCartMutation.isLoading ? 'Adding...' : 'Add to Cart'}
              </span>
            </button>
          </div>

          {/* Seller Info */}
          {book.seller && (
            <div className="border-t pt-4">
              <h3 className="font-medium text-gray-900 mb-2">Sold by</h3>
              <p className="text-gray-600">
                {book.seller.firstName} {book.seller.lastName}
              </p>
            </div>
          )}
        </div>
      </div>

      {/* Tabs */}
      <div className="border-b border-gray-200 mb-8">
        <nav className="-mb-px flex space-x-8">
          <button
            onClick={() => setActiveTab('description')}
            className={`py-2 px-1 border-b-2 font-medium text-sm ${
              activeTab === 'description'
                ? 'border-primary-500 text-primary-600'
                : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
            }`}
          >
            Description
          </button>
          <button
            onClick={() => setActiveTab('reviews')}
            className={`py-2 px-1 border-b-2 font-medium text-sm ${
              activeTab === 'reviews'
                ? 'border-primary-500 text-primary-600'
                : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
            }`}
          >
            Reviews ({reviews.length})
          </button>
        </nav>
      </div>

      {/* Tab Content */}
      <div className="mb-12">
        {activeTab === 'description' ? (
          <div className="prose max-w-none">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">About this book</h3>
            {book.description ? (
              <p className="text-gray-700 leading-relaxed">{book.description}</p>
            ) : (
              <p className="text-gray-500 italic">No description available.</p>
            )}
          </div>
        ) : (
          <div>
            <h3 className="text-lg font-semibold text-gray-900 mb-6">Customer Reviews</h3>
            {reviewsLoading ? (
              <div className="space-y-4">
                {[...Array(3)].map((_, index) => (
                  <div key={index} className="animate-pulse">
                    <div className="h-4 bg-gray-200 rounded w-1/4 mb-2"></div>
                    <div className="h-4 bg-gray-200 rounded w-full mb-2"></div>
                    <div className="h-4 bg-gray-200 rounded w-2/3"></div>
                  </div>
                ))}
              </div>
            ) : reviews.length === 0 ? (
              <p className="text-gray-500 italic">No reviews yet. Be the first to review this book!</p>
            ) : (
              <div className="space-y-6">
                {reviews.map((review: Review) => (
                  <div key={review.id} className="border-b border-gray-200 pb-6 last:border-b-0">
                    <div className="flex items-center justify-between mb-2">
                      <div className="flex items-center space-x-2">
                        <span className="font-medium text-gray-900">
                          {review.user.firstName} {review.user.lastName}
                        </span>
                        <div className="flex items-center">
                          {[...Array(5)].map((_, index) => (
                            <Star
                              key={index}
                              className={`h-4 w-4 ${
                                index < review.rating
                                  ? 'text-yellow-400 fill-current'
                                  : 'text-gray-300'
                              }`}
                            />
                          ))}
                        </div>
                      </div>
                      <span className="text-sm text-gray-500">
                        {new Date(review.createdAt).toLocaleDateString()}
                      </span>
                    </div>
                    {review.comment && (
                      <p className="text-gray-700">{review.comment}</p>
                    )}
                  </div>
                ))}
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default BookDetail;





