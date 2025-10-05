import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from 'react-query';
import { toast } from 'react-hot-toast';
import { Trash2, ShoppingCart, ArrowRight } from 'lucide-react';
import apiService from '../services/api';
import { Cart as CartType, Book } from '../types';

const Cart: React.FC = () => {
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const [isCheckingOut, setIsCheckingOut] = useState(false);

  // Fetch cart data
  const { data: cart, isLoading } = useQuery('cart', () => apiService.getCart());

  // Remove from cart mutation
  const removeFromCartMutation = useMutation(
    (bookId: number) => apiService.removeFromCart(bookId),
    {
      onSuccess: () => {
        toast.success('Item removed from cart');
        queryClient.invalidateQueries('cart');
      },
      onError: () => {
        toast.error('Failed to remove item from cart');
      },
    }
  );

  // Create order mutation
  const createOrderMutation = useMutation(
    (orderData: any) => apiService.createOrder(orderData),
    {
      onSuccess: () => {
        toast.success('Order placed successfully!');
        queryClient.invalidateQueries('cart');
        queryClient.invalidateQueries('orders');
        navigate('/orders');
      },
      onError: () => {
        toast.error('Failed to place order');
      },
    }
  );

  const handleRemoveFromCart = (bookId: number) => {
    removeFromCartMutation.mutate(bookId);
  };

  const handleCheckout = () => {
    if (!cart?.books || cart.books.length === 0) {
      toast.error('Your cart is empty');
      return;
    }

    setIsCheckingOut(true);
    
    const orderData = {
      bookIds: cart.books.map((book: Book) => book.id),
      shippingAddress: 'Default Address', // This should come from user profile or form
      totalAmount: cart.books.reduce((sum: number, book: Book) => sum + book.price, 0),
    };

    createOrderMutation.mutate(orderData);
  };

  const totalAmount = cart?.books?.reduce((sum: number, book: Book) => sum + book.price, 0) || 0;

  if (isLoading) {
    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="animate-pulse">
          <div className="h-8 bg-gray-200 rounded w-1/4 mb-8"></div>
          <div className="space-y-4">
            {[...Array(3)].map((_, index) => (
              <div key={index} className="flex space-x-4">
                <div className="h-24 w-24 bg-gray-200 rounded"></div>
                <div className="flex-1 space-y-2">
                  <div className="h-4 bg-gray-200 rounded w-3/4"></div>
                  <div className="h-4 bg-gray-200 rounded w-1/2"></div>
                  <div className="h-4 bg-gray-200 rounded w-1/4"></div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    );
  }

  if (!cart || !cart.books || cart.books.length === 0) {
    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="text-center">
          <div className="text-gray-400 mb-4">
            <ShoppingCart className="h-16 w-16 mx-auto" />
          </div>
          <h2 className="text-2xl font-bold text-gray-900 mb-4">Your cart is empty</h2>
          <p className="text-gray-600 mb-8">
            Looks like you haven't added any books to your cart yet.
          </p>
          <Link
            to="/books"
            className="btn btn-primary inline-flex items-center space-x-2"
          >
            <span>Browse Books</span>
            <ArrowRight className="h-4 w-4" />
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900">Shopping Cart</h1>
        <p className="text-gray-600 mt-2">
          {cart.books.length} item{cart.books.length !== 1 ? 's' : ''} in your cart
        </p>
      </div>

      <div className="grid lg:grid-cols-3 gap-8">
        {/* Cart Items */}
        <div className="lg:col-span-2">
          <div className="bg-white rounded-lg shadow-sm border border-gray-200">
            <div className="card-header">
              <h2 className="text-lg font-semibold text-gray-900">Cart Items</h2>
            </div>
            <div className="divide-y divide-gray-200">
              {cart.books.map((book: Book) => (
                <div key={book.id} className="p-6 flex space-x-4">
                  <div className="flex-shrink-0">
                    <img
                      src={book.imagePath || '/placeholder-book.jpg'}
                      alt={book.title}
                      className="w-20 h-24 object-cover rounded"
                      onError={(e) => {
                        const target = e.target as HTMLImageElement;
                        target.src = '/placeholder-book.jpg';
                      }}
                    />
                  </div>
                  <div className="flex-1 min-w-0">
                    <div className="flex justify-between">
                      <div>
                        <h3 className="text-lg font-medium text-gray-900 mb-1">
                          {book.title}
                        </h3>
                        <p className="text-gray-600 mb-2">by {book.author}</p>
                        {book.category && (
                          <span className="inline-block bg-primary-100 text-primary-800 px-2 py-1 rounded text-xs font-medium">
                            {book.category.name}
                          </span>
                        )}
                      </div>
                      <div className="text-right">
                        <p className="text-lg font-semibold text-primary-600">
                          ${book.price}
                        </p>
                        <button
                          onClick={() => handleRemoveFromCart(book.id)}
                          disabled={removeFromCartMutation.isLoading}
                          className="mt-2 text-red-600 hover:text-red-800 flex items-center space-x-1 text-sm"
                        >
                          <Trash2 className="h-4 w-4" />
                          <span>Remove</span>
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* Order Summary */}
        <div className="lg:col-span-1">
          <div className="bg-white rounded-lg shadow-sm border border-gray-200 sticky top-8">
            <div className="card-header">
              <h2 className="text-lg font-semibold text-gray-900">Order Summary</h2>
            </div>
            <div className="card-body">
              <div className="space-y-4">
                <div className="flex justify-between">
                  <span className="text-gray-600">Subtotal</span>
                  <span className="font-medium">${totalAmount.toFixed(2)}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-gray-600">Shipping</span>
                  <span className="font-medium">Free</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-gray-600">Tax</span>
                  <span className="font-medium">$0.00</span>
                </div>
                <div className="border-t pt-4">
                  <div className="flex justify-between">
                    <span className="text-lg font-semibold text-gray-900">Total</span>
                    <span className="text-lg font-semibold text-primary-600">
                      ${totalAmount.toFixed(2)}
                    </span>
                  </div>
                </div>
              </div>
            </div>
            <div className="card-footer">
              <button
                onClick={handleCheckout}
                disabled={createOrderMutation.isLoading || cart.books.length === 0}
                className="w-full btn btn-primary flex items-center justify-center space-x-2"
              >
                {createOrderMutation.isLoading ? (
                  <>
                    <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white"></div>
                    <span>Processing...</span>
                  </>
                ) : (
                  <>
                    <span>Proceed to Checkout</span>
                    <ArrowRight className="h-4 w-4" />
                  </>
                )}
              </button>
              <div className="mt-4 text-center">
                <Link
                  to="/books"
                  className="text-sm text-primary-600 hover:text-primary-700"
                >
                  Continue Shopping
                </Link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Cart;





