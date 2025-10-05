// User related types
export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  password?: string;
  phone?: string;
  address?: string;
  role: UserRole;
  createdAt: string;
  updatedAt: string;
}

export enum UserRole {
  USER = 'USER',
  ADMIN = 'ADMIN'
}

// Book related types
export interface Book {
  id: number;
  title: string;
  author: string;
  description?: string;
  price: number;
  imagePath?: string;
  filePath?: string;
  approved: boolean;
  available: boolean;
  category: Category;
  seller: User;
  createdAt: string;
  updatedAt: string;
}

export interface BookDTO {
  id?: number;
  title: string;
  author: string;
  description?: string;
  price: number;
  imagePath?: string;
  filePath?: string;
  approved?: boolean;
  available?: boolean;
  categoryId?: number;
  sellerId?: number;
}

// Category related types
export interface Category {
  id: number;
  name: string;
  description?: string;
  createdAt: string;
  updatedAt: string;
}

export interface CategoryDTO {
  id?: number;
  name: string;
  description?: string;
}

// Cart related types
export interface Cart {
  id: number;
  user: User;
  books: Book[];
  createdAt: string;
  updatedAt: string;
}

export interface CartDTO {
  id?: number;
  userId?: number;
  bookIds?: number[];
}

// Order related types
export interface Order {
  id: number;
  user: User;
  books: Book[];
  totalAmount: number;
  status: OrderStatus;
  shippingAddress: string;
  createdAt: string;
  updatedAt: string;
}

export enum OrderStatus {
  PENDING = 'PENDING',
  CONFIRMED = 'CONFIRMED',
  SHIPPED = 'SHIPPED',
  DELIVERED = 'DELIVERED',
  CANCELLED = 'CANCELLED'
}

export interface OrderDTO {
  id?: number;
  userId?: number;
  bookIds?: number[];
  totalAmount?: number;
  status?: OrderStatus;
  shippingAddress: string;
}

// Review related types
export interface Review {
  id: number;
  user: User;
  book: Book;
  rating: number;
  comment?: string;
  createdAt: string;
  updatedAt: string;
}

export interface ReviewDTO {
  id?: number;
  userId?: number;
  bookId: number;
  rating: number;
  comment?: string;
}

// Transaction related types
export interface Transaction {
  id: number;
  order: Order;
  amount: number;
  status: TransactionStatus;
  paymentMethod: string;
  createdAt: string;
  updatedAt: string;
}

export enum TransactionStatus {
  PENDING = 'PENDING',
  COMPLETED = 'COMPLETED',
  FAILED = 'FAILED',
  REFUNDED = 'REFUNDED'
}

export interface TransactionDTO {
  id?: number;
  orderId: number;
  amount: number;
  status?: TransactionStatus;
  paymentMethod: string;
}

// Authentication types
export interface AuthRequest {
  email: string;
  password: string;
}

export interface AuthResponse {
  message: string;
  jwt: string;
}

export interface SignupResponse {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  dob: string;
  userRole: UserRole;
}

export interface SignupRequest {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  userRole: UserRole;
  dob: string;
}

// API Response types
export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data?: T;
  error?: string;
}

// Pagination types
export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}


