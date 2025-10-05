import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { useQuery } from 'react-query';
import { Search, Filter, Star, ShoppingCart } from 'lucide-react';
import apiService from '../services/api';
import { Book, Category } from '../types';

const BookList: React.FC = () => {
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCategory, setSelectedCategory] = useState<string>('');
  const [sortBy, setSortBy] = useState<string>('title');
  const [currentPage, setCurrentPage] = useState(1);
  const booksPerPage = 12;

  // Fetch books and categories
  const { data: books = [], isLoading: booksLoading } = useQuery(
    ['books', searchTerm, selectedCategory, sortBy],
    () => apiService.getBooks({
      search: searchTerm,
      category: selectedCategory,
      sort: sortBy,
      approved: true,
      available: true,
    })
  );

  const { data: categories = [], isLoading: categoriesLoading } = useQuery(
    'categories',
    () => apiService.getCategories()
  );

  // Pagination
  const indexOfLastBook = currentPage * booksPerPage;
  const indexOfFirstBook = indexOfLastBook - booksPerPage;
  const currentBooks = books.slice(indexOfFirstBook, indexOfLastBook);
  const totalPages = Math.ceil(books.length / booksPerPage);

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    setCurrentPage(1);
  };

  const handleCategoryChange = (categoryId: string) => {
    setSelectedCategory(categoryId);
    setCurrentPage(1);
  };

  const handleSortChange = (sort: string) => {
    setSortBy(sort);
    setCurrentPage(1);
  };

  const clearFilters = () => {
    setSearchTerm('');
    setSelectedCategory('');
    setSortBy('title');
    setCurrentPage(1);
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      {/* Header */}
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-2">Browse Books</h1>
        <p className="text-gray-600">
          Discover thousands of books from various genres and authors
        </p>
      </div>

      {/* Search and Filters */}
      <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6 mb-8">
        <form onSubmit={handleSearch} className="mb-6">
          <div className="flex gap-4">
            <div className="flex-1 relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-400" />
              <input
                type="text"
                placeholder="Search books by title, author, or description..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="input pl-10"
              />
            </div>
            <button
              type="submit"
              className="btn btn-primary flex items-center space-x-2"
            >
              <Search className="h-4 w-4" />
              <span>Search</span>
            </button>
          </div>
        </form>

        <div className="flex flex-wrap gap-4 items-center">
          <div className="flex items-center space-x-2">
            <Filter className="h-5 w-5 text-gray-500" />
            <span className="text-sm font-medium text-gray-700">Filters:</span>
          </div>

          {/* Category Filter */}
          <div>
            <select
              value={selectedCategory}
              onChange={(e) => handleCategoryChange(e.target.value)}
              className="input text-sm"
            >
              <option value="">All Categories</option>
              {categories.map((category: Category) => (
                <option key={category.id} value={category.id}>
                  {category.name}
                </option>
              ))}
            </select>
          </div>

          {/* Sort Filter */}
          <div>
            <select
              value={sortBy}
              onChange={(e) => handleSortChange(e.target.value)}
              className="input text-sm"
            >
              <option value="title">Sort by Title</option>
              <option value="author">Sort by Author</option>
              <option value="price">Sort by Price (Low to High)</option>
              <option value="price_desc">Sort by Price (High to Low)</option>
              <option value="createdAt">Sort by Newest</option>
            </select>
          </div>

          {/* Clear Filters */}
          {(searchTerm || selectedCategory || sortBy !== 'title') && (
            <button
              onClick={clearFilters}
              className="text-sm text-primary-600 hover:text-primary-700 font-medium"
            >
              Clear Filters
            </button>
          )}
        </div>
      </div>

      {/* Results Count */}
      <div className="flex justify-between items-center mb-6">
        <p className="text-gray-600">
          {booksLoading ? 'Loading...' : `${books.length} books found`}
        </p>
      </div>

      {/* Books Grid */}
      {booksLoading ? (
        <div className="grid md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          {[...Array(8)].map((_, index) => (
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
      ) : currentBooks.length === 0 ? (
        <div className="text-center py-12">
          <div className="text-gray-400 mb-4">
            <Search className="h-16 w-16 mx-auto" />
          </div>
          <h3 className="text-lg font-medium text-gray-900 mb-2">
            No books found
          </h3>
          <p className="text-gray-600 mb-4">
            Try adjusting your search terms or filters
          </p>
          <button
            onClick={clearFilters}
            className="btn btn-primary"
          >
            Clear Filters
          </button>
        </div>
      ) : (
        <>
          <div className="grid md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            {currentBooks.map((book: Book) => (
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
                  {book.category && (
                    <div className="absolute top-2 left-2 bg-white bg-opacity-90 text-gray-700 px-2 py-1 rounded text-xs font-medium">
                      {book.category.name}
                    </div>
                  )}
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
                    <div className="flex items-center space-x-1">
                      <Star className="h-4 w-4 text-yellow-400 fill-current" />
                      <span className="text-sm text-gray-600">4.5</span>
                    </div>
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

          {/* Pagination */}
          {totalPages > 1 && (
            <div className="flex justify-center mt-8">
              <nav className="flex items-center space-x-2">
                <button
                  onClick={() => setCurrentPage(Math.max(1, currentPage - 1))}
                  disabled={currentPage === 1}
                  className="px-3 py-2 text-sm font-medium text-gray-500 bg-white border border-gray-300 rounded-md hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  Previous
                </button>
                
                {[...Array(totalPages)].map((_, index) => {
                  const page = index + 1;
                  const isCurrentPage = page === currentPage;
                  
                  return (
                    <button
                      key={page}
                      onClick={() => setCurrentPage(page)}
                      className={`px-3 py-2 text-sm font-medium rounded-md ${
                        isCurrentPage
                          ? 'bg-primary-600 text-white'
                          : 'text-gray-500 bg-white border border-gray-300 hover:bg-gray-50'
                      }`}
                    >
                      {page}
                    </button>
                  );
                })}
                
                <button
                  onClick={() => setCurrentPage(Math.min(totalPages, currentPage + 1))}
                  disabled={currentPage === totalPages}
                  className="px-3 py-2 text-sm font-medium text-gray-500 bg-white border border-gray-300 rounded-md hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  Next
                </button>
              </nav>
            </div>
          )}
        </>
      )}
    </div>
  );
};

export default BookList;





