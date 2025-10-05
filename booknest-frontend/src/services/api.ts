import axios, { AxiosInstance, AxiosResponse } from 'axios';
import { AuthResponse, User, SignupResponse } from '../types';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8081/api';

class ApiService {
  private api: AxiosInstance;

  constructor() {
    this.api = axios.create({
      baseURL: API_BASE_URL,
      headers: {
        'Content-Type': 'application/json',
      },
    });

    // Request interceptor to add auth token
    this.api.interceptors.request.use(
      (config) => {
        const token = localStorage.getItem('token');
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
      },
      (error) => {
        return Promise.reject(error);
      }
    );

    // Response interceptor to handle auth errors
    this.api.interceptors.response.use(
      (response) => response,
      (error) => {
        if (error.response?.status === 401) {
          localStorage.removeItem('token');
          localStorage.removeItem('user');
          window.location.href = '/login';
        }
        return Promise.reject(error);
      }
    );
  }

  // Authentication methods
  async login(email: string, password: string): Promise<AuthResponse> {
    const response: AxiosResponse<AuthResponse> = await this.api.post('/users/signin', {
      email,
      password,
    });
    return response.data;
  }

  async signup(userData: any): Promise<SignupResponse> {
    const response: AxiosResponse<SignupResponse> = await this.api.post('/users/signup', userData);
    return response.data;
  }

  async logout(): Promise<void> {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  }

  // User methods
  async getCurrentUser(): Promise<User> {
    const response: AxiosResponse<User> = await this.api.get('/users/profile');
    return response.data;
  }

  async updateProfile(userData: Partial<User>): Promise<User> {
    const response: AxiosResponse<User> = await this.api.put('/users/profile', userData);
    return response.data;
  }

  // Book methods
  async getBooks(params?: any): Promise<any[]> {
    const response: AxiosResponse<any[]> = await this.api.get('/books', { params });
    return response.data;
  }

  async getBook(id: number): Promise<any> {
    const response: AxiosResponse<any> = await this.api.get(`/books/${id}`);
    return response.data;
  }

  async createBook(bookData: any): Promise<any> {
    const response: AxiosResponse<any> = await this.api.post('/books', bookData);
    return response.data;
  }

  async updateBook(id: number, bookData: any): Promise<any> {
    const response: AxiosResponse<any> = await this.api.put(`/books/${id}`, bookData);
    return response.data;
  }

  async deleteBook(id: number): Promise<void> {
    await this.api.delete(`/books/${id}`);
  }

  // Category methods
  async getCategories(): Promise<any[]> {
    const response: AxiosResponse<any[]> = await this.api.get('/categories');
    return response.data;
  }

  // Cart methods
  async getCart(): Promise<any> {
    const response: AxiosResponse<any> = await this.api.get('/cart');
    return response.data;
  }

  async addToCart(bookId: number): Promise<any> {
    const response: AxiosResponse<any> = await this.api.post('/cart/books', { bookId });
    return response.data;
  }

  async removeFromCart(bookId: number): Promise<any> {
    const response: AxiosResponse<any> = await this.api.delete(`/cart/books/${bookId}`);
    return response.data;
  }

  // Order methods
  async getOrders(): Promise<any[]> {
    const response: AxiosResponse<any[]> = await this.api.get('/orders');
    return response.data;
  }

  async createOrder(orderData: any): Promise<any> {
    const response: AxiosResponse<any> = await this.api.post('/orders', orderData);
    return response.data;
  }

  async getOrder(id: number): Promise<any> {
    const response: AxiosResponse<any> = await this.api.get(`/orders/${id}`);
    return response.data;
  }

  // Review methods
  async getBookReviews(bookId: number): Promise<any[]> {
    const response: AxiosResponse<any[]> = await this.api.get(`/books/${bookId}/reviews`);
    return response.data;
  }

  async createReview(bookId: number, reviewData: any): Promise<any> {
    const response: AxiosResponse<any> = await this.api.post(`/books/${bookId}/reviews`, reviewData);
    return response.data;
  }

  // File upload
  async uploadFile(file: File, type: 'image' | 'document'): Promise<string> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('type', type);

    const response: AxiosResponse<{ filePath: string }> = await this.api.post('/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    return response.data.filePath;
  }
}

export const apiService = new ApiService();
export default apiService;

// Export individual functions for easier imports
export const loginUser = (email: string, password: string) => apiService.login(email, password);
export const signupUser = (userData: any) => apiService.signup(userData);
export const updateProfile = (userData: Partial<User>) => apiService.updateProfile(userData);


