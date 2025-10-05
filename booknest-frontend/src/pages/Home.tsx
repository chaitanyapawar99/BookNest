import React from 'react';
import { Link } from 'react-router-dom';
import { useQuery } from 'react-query';
import { BookOpen, Search, Star, ShoppingCart } from 'lucide-react';
import apiService from '../services/api';
import { Book } from '../types';

const Home: React.FC = () => {
  const { data: books = [], isLoading } = useQuery('featured-books', () =>
    apiService.getBooks({ approved: true, available: true })
  );

  const featuredBooks = books.slice(0, 6);

  return (
    <div className="space-y-12">
      {/* Hero Section */}
      <section className="relative bg-gradient-to-r from-primary-600 to-primary-800 text-white py-20">
        <div className="absolute inset-0 bg-black opacity-20"></div>
        <div className="relative max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <div className="flex justify-center mb-6">
            <BookOpen className="h-16 w-16 text-white" />
          </div>
          <h1 className="text-4xl md:text-6xl font-bold mb-6 text-shadow">
            Welcome to BookNest
          </h1>
          <p className="text-xl md:text-2xl mb-8 text-primary-100 max-w-3xl mx-auto">
            Discover, buy, and sell books in our vibrant community. 
            From rare finds to bestsellers, find your next favorite read.
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <Link
              to="/books"
              className="bg-white text-primary-600 px-8 py-3 rounded-lg font-semibold hover:bg-gray-100 transition-colors inline-flex items-center justify-center space-x-2"
            >
              <Search className="h-5 w-5" />
              <span>Browse Books</span>
            </Link>
            <Link
              to="/signup"
              className="border-2 border-white text-white px-8 py-3 rounded-lg font-semibold hover:bg-white hover:text-primary-600 transition-colors"
            >
              Join Our Community
            </Link>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="py-16">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <h2 className="text-3xl font-bold text-center mb-12 text-gray-900">
            Why Choose BookNest?
          </h2>
          <div className="grid md:grid-cols-3 gap-8">
            <div className="text-center">
              <div className="bg-primary-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
                <BookOpen className="h-8 w-8 text-primary-600" />
              </div>
              <h3 className="text-xl font-semibold mb-2 text-gray-900">
                Vast Collection
              </h3>
              <p className="text-gray-600">
                Access thousands of books from various genres, authors, and publishers.
              </p>
            </div>
            <div className="text-center">
              <div className="bg-primary-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
                <ShoppingCart className="h-8 w-8 text-primary-600" />
              </div>
              <h3 className="text-xl font-semibold mb-2 text-gray-900">
                Easy Shopping
              </h3>
              <p className="text-gray-600">
                Simple and secure checkout process with multiple payment options.
              </p>
            </div>
            <div className="text-center">
              <div className="bg-primary-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
                <Star className="h-8 w-8 text-primary-600" />
              </div>
              <h3 className="text-xl font-semibold mb-2 text-gray-900">
                Community Reviews
              </h3>
              <p className="text-gray-600">
                Read authentic reviews from fellow book lovers before making a purchase.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* Featured Books Section */}
      <section className="py-16 bg-gray-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center mb-8">
            <h2 className="text-3xl font-bold text-gray-900">
              Featured Books
            </h2>
            <Link
              to="/books"
              className="text-primary-600 hover:text-primary-700 font-medium"
            >
              View All Books →
            </Link>
          </div>

          {isLoading ? (
            <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
              {[...Array(6)].map((_, index) => (
                <div key={index} className="card animate-pulse">
                  <div className="h-48 bg-gray-200 rounded-t-lg"></div>
                  <div className="card-body">
                    <div className="h-4 bg-gray-200 rounded mb-2"></div>
                    <div className="h-4 bg-gray-200 rounded mb-2 w-3/4"></div>
                    <div className="h-4 bg-gray-200 rounded w-1/2"></div>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
              {featuredBooks.map((book: Book) => (
                <div key={book.id} className="card hover:shadow-lg transition-shadow">
                  <div className="relative">
                    <img
                      src={book.imagePath || '/placeholder-book.jpg'}
                      alt={book.title}
                      className="w-full h-48 object-cover rounded-t-lg"
                      onError={(e) => {
                        const target = e.target as HTMLImageElement;
                        target.src = '/placeholder-book.jpg';
                      }}
                    />
                    <div className="absolute top-2 right-2 bg-primary-600 text-white px-2 py-1 rounded text-sm font-medium">
                      ${book.price}
                    </div>
                  </div>
                  <div className="card-body">
                    <h3 className="text-lg font-semibold text-gray-900 mb-2 line-clamp-2">
                      {book.title}
                    </h3>
                    <p className="text-gray-600 mb-2">by {book.author}</p>
                    {book.description && (
                      <p className="text-gray-500 text-sm line-clamp-3 mb-4">
                        {book.description}
                      </p>
                    )}
                    <div className="flex justify-between items-center">
                      <span className="text-sm text-gray-500">
                        {book.category?.name}
                      </span>
                      <Link
                        to={`/books/${book.id}`}
                        className="btn btn-primary text-sm"
                      >
                        View Details
                      </Link>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </section>

      {/* CTA Section */}
      <section className="py-16 bg-primary-600 text-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <h2 className="text-3xl font-bold mb-4">
            Ready to Start Your Reading Journey?
          </h2>
          <p className="text-xl mb-8 text-primary-100">
            Join thousands of book lovers and discover your next favorite read.
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <Link
              to="/signup"
              className="bg-white text-primary-600 px-8 py-3 rounded-lg font-semibold hover:bg-gray-100 transition-colors"
            >
              Get Started
            </Link>
            <Link
              to="/books"
              className="border-2 border-white text-white px-8 py-3 rounded-lg font-semibold hover:bg-white hover:text-primary-600 transition-colors"
            >
              Browse Books
            </Link>
          </div>
        </div>
      </section>
    </div>
  );
};

export default Home;





