import React from 'react';
import { Link } from 'react-router-dom';
import { useQuery } from 'react-query';
import { Package, Calendar, MapPin, DollarSign } from 'lucide-react';
import apiService from '../services/api';
import { Order, OrderStatus } from '../types';

const Orders: React.FC = () => {
  const { data: orders = [], isLoading } = useQuery('orders', () => apiService.getOrders());

  const getStatusColor = (status: OrderStatus) => {
    switch (status) {
      case OrderStatus.PENDING:
        return 'bg-yellow-100 text-yellow-800';
      case OrderStatus.CONFIRMED:
        return 'bg-blue-100 text-blue-800';
      case OrderStatus.SHIPPED:
        return 'bg-purple-100 text-purple-800';
      case OrderStatus.DELIVERED:
        return 'bg-green-100 text-green-800';
      case OrderStatus.CANCELLED:
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  const getStatusIcon = (status: OrderStatus) => {
    switch (status) {
      case OrderStatus.PENDING:
        return '⏳';
      case OrderStatus.CONFIRMED:
        return '✅';
      case OrderStatus.SHIPPED:
        return '📦';
      case OrderStatus.DELIVERED:
        return '🎉';
      case OrderStatus.CANCELLED:
        return '❌';
      default:
        return '📋';
    }
  };

  if (isLoading) {
    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="animate-pulse">
          <div className="h-8 bg-gray-200 rounded w-1/4 mb-8"></div>
          <div className="space-y-4">
            {[...Array(3)].map((_, index) => (
              <div key={index} className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
                <div className="h-4 bg-gray-200 rounded w-1/3 mb-4"></div>
                <div className="space-y-2">
                  <div className="h-4 bg-gray-200 rounded w-full"></div>
                  <div className="h-4 bg-gray-200 rounded w-2/3"></div>
                  <div className="h-4 bg-gray-200 rounded w-1/2"></div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    );
  }

  if (orders.length === 0) {
    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="text-center">
          <div className="text-gray-400 mb-4">
            <Package className="h-16 w-16 mx-auto" />
          </div>
          <h2 className="text-2xl font-bold text-gray-900 mb-4">No orders yet</h2>
          <p className="text-gray-600 mb-8">
            You haven't placed any orders yet. Start shopping to see your order history here.
          </p>
          <Link
            to="/books"
            className="btn btn-primary"
          >
            Browse Books
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900">My Orders</h1>
        <p className="text-gray-600 mt-2">
          Track your order history and current orders
        </p>
      </div>

      <div className="space-y-6">
        {orders.map((order: Order) => (
          <div key={order.id} className="bg-white rounded-lg shadow-sm border border-gray-200">
            <div className="card-header">
              <div className="flex justify-between items-start">
                <div>
                  <h3 className="text-lg font-semibold text-gray-900">
                    Order #{order.id}
                  </h3>
                  <div className="flex items-center space-x-4 mt-2 text-sm text-gray-600">
                    <div className="flex items-center space-x-1">
                      <Calendar className="h-4 w-4" />
                      <span>
                        {new Date(order.createdAt).toLocaleDateString()}
                      </span>
                    </div>
                    <div className="flex items-center space-x-1">
                      <DollarSign className="h-4 w-4" />
                      <span>${order.totalAmount.toFixed(2)}</span>
                    </div>
                  </div>
                </div>
                <div className="flex items-center space-x-2">
                  <span className="text-2xl">{getStatusIcon(order.status)}</span>
                  <span className={`px-3 py-1 rounded-full text-sm font-medium ${getStatusColor(order.status)}`}>
                    {order.status}
                  </span>
                </div>
              </div>
            </div>

            <div className="card-body">
              {/* Shipping Address */}
              <div className="mb-6">
                <div className="flex items-start space-x-2">
                  <MapPin className="h-5 w-5 text-gray-400 mt-0.5" />
                  <div>
                    <h4 className="font-medium text-gray-900 mb-1">Shipping Address</h4>
                    <p className="text-gray-600">{order.shippingAddress}</p>
                  </div>
                </div>
              </div>

              {/* Order Items */}
              <div>
                <h4 className="font-medium text-gray-900 mb-3">Order Items</h4>
                <div className="space-y-3">
                  {order.books.map((book) => (
                    <div key={book.id} className="flex items-center space-x-4">
                      <img
                        src={book.imagePath || '/placeholder-book.jpg'}
                        alt={book.title}
                        className="w-16 h-20 object-cover rounded"
                        onError={(e) => {
                          const target = e.target as HTMLImageElement;
                          target.src = '/placeholder-book.jpg';
                        }}
                      />
                      <div className="flex-1 min-w-0">
                        <h5 className="text-sm font-medium text-gray-900 truncate">
                          {book.title}
                        </h5>
                        <p className="text-sm text-gray-600">by {book.author}</p>
                        {book.category && (
                          <span className="inline-block bg-gray-100 text-gray-700 px-2 py-1 rounded text-xs mt-1">
                            {book.category.name}
                          </span>
                        )}
                      </div>
                      <div className="text-right">
                        <p className="text-sm font-medium text-gray-900">
                          ${book.price}
                        </p>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </div>

            <div className="card-footer">
              <div className="flex justify-between items-center">
                <div className="text-sm text-gray-600">
                  <span className="font-medium">Total:</span> ${order.totalAmount.toFixed(2)}
                </div>
                <div className="flex space-x-2">
                  <Link
                    to={`/orders/${order.id}`}
                    className="btn btn-secondary text-sm"
                  >
                    View Details
                  </Link>
                  {order.status === OrderStatus.DELIVERED && (
                    <button className="btn btn-primary text-sm">
                      Write Review
                    </button>
                  )}
                </div>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Orders;





