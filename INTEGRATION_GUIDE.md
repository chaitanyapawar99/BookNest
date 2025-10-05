# Frontend-Backend Integration Guide

## Overview
This guide explains how to integrate the React frontend with the Spring Boot backend for the BookNest application.

## Current Configuration

### Backend (Spring Boot)
- **Port**: 8081
- **Base URL**: `http://localhost:8081`
- **API Prefix**: `/api`
- **Database**: MySQL (BookNest)

### Frontend (React)
- **Port**: 3000
- **Proxy**: `http://localhost:8081`
- **API Base URL**: `http://localhost:8081/api`

## Quick Start

### Option 1: Use the Integration Script
```bash
# Windows
start-integration.bat

# This will start both backend and frontend automatically
```

### Option 2: Manual Start
```bash
# Terminal 1: Start Backend
cd food_delivery_backend_secured
mvnw spring-boot:run

# Terminal 2: Start Frontend
cd booknest-frontend
npm start
```

## API Endpoints Mapping

### Authentication
| Frontend Call | Backend Endpoint | Status |
|---------------|------------------|--------|
| `POST /users/signin` | `POST /api/users/signin` | ✅ Working |
| `POST /users/signup` | `POST /api/users/signup` | ✅ Working |

### User Management
| Frontend Call | Backend Endpoint | Status |
|---------------|------------------|--------|
| `GET /users/profile` | `GET /api/users/profile` | ✅ Added |
| `PUT /users/profile` | `PUT /api/users/profile` | ✅ Added |
| `GET /users/{id}` | `GET /api/users/{id}` | ✅ Working |

### Books
| Frontend Call | Backend Endpoint | Status |
|---------------|------------------|--------|
| `GET /books` | `GET /api/books` | ✅ Working |
| `GET /books/{id}` | `GET /api/books/{id}` | ✅ Working |
| `POST /books` | `POST /api/books` | ✅ Working |
| `PUT /books/{id}` | `PUT /api/books/{id}` | ✅ Working |
| `DELETE /books/{id}` | `DELETE /api/books/{id}` | ✅ Working |

### Categories
| Frontend Call | Backend Endpoint | Status |
|---------------|------------------|--------|
| `GET /categories` | `GET /api/categories` | ✅ Working |

### Cart
| Frontend Call | Backend Endpoint | Status |
|---------------|------------------|--------|
| `GET /cart` | `GET /api/cart` | ✅ Fixed |
| `POST /cart/books` | `POST /api/cart/books` | ✅ Fixed |
| `DELETE /cart/books/{bookId}` | `DELETE /api/cart/books/{bookId}` | ✅ Fixed |

### Orders
| Frontend Call | Backend Endpoint | Status |
|---------------|------------------|--------|
| `GET /orders` | `GET /api/orders` | ✅ Fixed |
| `POST /orders` | `POST /api/orders` | ✅ Fixed |
| `GET /orders/{id}` | `GET /api/orders/{id}` | ✅ Added |

### Reviews
| Frontend Call | Backend Endpoint | Status |
|---------------|------------------|--------|
| `GET /books/{id}/reviews` | `GET /api/books/{id}/reviews` | ✅ Added |
| `POST /books/{id}/reviews` | `POST /api/books/{id}/reviews` | ✅ Added |

### File Upload
| Frontend Call | Backend Endpoint | Status |
|---------------|------------------|--------|
| `POST /upload` | `POST /api/upload` | ✅ Added |

## CORS Configuration
The backend is configured to allow requests from `http://localhost:3000` (React default port).

## Authentication Flow
1. User signs up/signs in via frontend
2. Backend returns JWT token
3. Frontend stores token in localStorage
4. Frontend includes token in Authorization header for subsequent requests
5. Backend validates token and processes requests

## Environment Variables
The frontend uses the following environment variables:
- `REACT_APP_API_URL`: Override the default API URL (optional)

## Troubleshooting

### Common Issues

1. **CORS Errors**
   - Ensure backend is running on port 8081
   - Check CORS configuration in `CorsConfig.java`

2. **Authentication Errors**
   - Verify JWT token is being sent in Authorization header
   - Check token expiration time in `application.properties`

3. **Port Conflicts**
   - Backend: Change port in `application.properties` if 8081 is busy
   - Frontend: React will automatically use next available port if 3000 is busy

4. **Database Connection**
   - Ensure MySQL is running
   - Check database credentials in `application.properties`

### Testing the Integration

1. **Backend Health Check**
   ```bash
   curl http://localhost:8081/api/books
   ```

2. **Frontend Health Check**
   - Open http://localhost:3000 in browser
   - Check browser console for any errors

3. **Authentication Test**
   - Try to sign up a new user
   - Try to sign in with existing user
   - Check if JWT token is stored in localStorage

## Next Steps

### ✅ All Integration Complete!
1. ✅ CartController updated to match frontend expectations
2. ✅ OrderController updated to match frontend expectations
3. ✅ Review endpoints added
4. ✅ File upload functionality added

### Future Enhancements
1. Add proper error handling and user feedback
2. Implement real-time updates
3. Add pagination for large datasets
4. Implement caching strategies
5. Add comprehensive logging and monitoring

## Development Workflow

1. **Backend Changes**: Modify Spring Boot controllers/services
2. **Frontend Changes**: Update React components and API calls
3. **Testing**: Use the integration script to test both together
4. **Database**: Changes are automatically applied via Hibernate

## File Structure
```
food_delivery_backend_secured/
├── src/main/java/com/cdac/
│   ├── controller/          # REST API endpoints
│   ├── service/            # Business logic
│   ├── dao/               # Data access
│   ├── entities/          # Database models
│   ├── dto/               # Data transfer objects
│   └── security/          # JWT and security config
├── booknest-frontend/
│   ├── src/
│   │   ├── components/    # React components
│   │   ├── pages/         # Page components
│   │   ├── services/      # API service layer
│   │   └── types/         # TypeScript types
│   └── package.json
└── start-integration.bat  # Integration script
```
