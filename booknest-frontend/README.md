# BookNest Frontend

A modern React application for the BookNest digital bookstore platform. This frontend provides a beautiful and intuitive user interface for browsing, buying, and selling books.

## Features

### For Customers
- **Browse Books**: Search and filter books by category, author, and price
- **Book Details**: View comprehensive book information, reviews, and ratings
- **Shopping Cart**: Add books to cart and manage purchases
- **Order Management**: Track order history and status
- **User Profile**: Manage personal information and preferences
- **Reviews**: Read and write book reviews

### For Sellers
- **Add Books**: Upload new books with cover images and files
- **Book Management**: Manage your book listings
- **Sales Tracking**: Monitor your book sales

### For Admins
- **User Management**: Manage user accounts and roles
- **Book Approval**: Review and approve book submissions
- **Platform Management**: Oversee the entire platform

## Technology Stack

- **React 18** - Modern React with hooks and functional components
- **TypeScript** - Type-safe JavaScript development
- **Tailwind CSS** - Utility-first CSS framework for styling
- **React Router** - Client-side routing
- **React Query** - Data fetching and caching
- **React Hook Form** - Form handling and validation
- **Axios** - HTTP client for API communication
- **Lucide React** - Beautiful icon library
- **React Hot Toast** - Toast notifications

## Project Structure

```
src/
├── components/          # Reusable UI components
├── contexts/           # React contexts (Auth, etc.)
├── hooks/              # Custom React hooks
├── pages/              # Page components
├── services/           # API services
├── types/              # TypeScript type definitions
├── utils/              # Utility functions
├── App.tsx            # Main application component
├── index.tsx          # Application entry point
└── index.css          # Global styles
```

## Getting Started

### Prerequisites

- Node.js (v16 or higher)
- npm or yarn
- Backend API running on `http://localhost:8080`

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd booknest-frontend
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Start the development server**
   ```bash
   npm start
   ```

4. **Open your browser**
   Navigate to `http://localhost:3000`

### Environment Variables

Create a `.env` file in the root directory:

```env
REACT_APP_API_URL=http://localhost:8080/api
```

## Available Scripts

- `npm start` - Start the development server
- `npm build` - Build the app for production
- `npm test` - Run tests
- `npm eject` - Eject from Create React App

## API Integration

The frontend integrates with the BookNest backend API. Key endpoints include:

- **Authentication**: `/api/auth/signin`, `/api/auth/signup`
- **Books**: `/api/books`
- **Categories**: `/api/categories`
- **Cart**: `/api/cart`
- **Orders**: `/api/orders`
- **Users**: `/api/users`

## Key Features Implementation

### Authentication
- JWT-based authentication
- Protected routes
- Automatic token refresh
- User context management

### State Management
- React Query for server state
- React Context for global state
- Local state for component-specific data

### Responsive Design
- Mobile-first approach
- Responsive navigation
- Adaptive layouts for all screen sizes

### Error Handling
- Global error boundaries
- Toast notifications
- Form validation
- API error handling

## Styling

The application uses Tailwind CSS with custom components:

- **Custom Components**: `.btn`, `.input`, `.card`
- **Color Scheme**: Primary blue theme with gray accents
- **Typography**: Inter font family
- **Icons**: Lucide React icons

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## Deployment

### Build for Production
```bash
npm run build
```

### Deploy to Netlify/Vercel
1. Connect your repository
2. Set build command: `npm run build`
3. Set publish directory: `build`

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## License

This project is licensed under the MIT License.

## Support

For support and questions, please contact the development team or create an issue in the repository.





