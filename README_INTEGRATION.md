# Frontend-Backend Integration Setup

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Node.js 16 or higher
- MySQL 8.0 or higher
- Maven

### 1. Database Setup
```sql
-- Create database (if not exists)
CREATE DATABASE IF NOT EXISTS BookNest;
```

### 2. Start the Integration
```bash
# Windows
start-integration.bat

# This will automatically start both backend and frontend
```

### 3. Test the Integration
```bash
# Windows
test-integration.bat
```

## 📋 Manual Setup

### Backend (Spring Boot)
```bash
# Navigate to project root
cd food_delivery_backend_secured

# Start the backend
mvnw spring-boot:run
```
- Backend will start on: http://localhost:8081
- API Documentation: http://localhost:8081/swagger-ui.html

### Frontend (React)
```bash
# Navigate to frontend directory
cd booknest-frontend

# Install dependencies (first time only)
npm install

# Start the frontend
npm start
```
- Frontend will start on: http://localhost:3000

## 🔧 Configuration

### Backend Configuration
- **Port**: 8081 (configurable in `application.properties`)
- **Database**: MySQL (BookNest)
- **JWT Secret**: Configured in `application.properties`
- **CORS**: Configured for `http://localhost:3000`

### Frontend Configuration
- **Port**: 3000 (React default)
- **API Base URL**: `http://localhost:8081/api`
- **Proxy**: `http://localhost:8081` (for development)

## 🔐 Authentication Flow

1. **Sign Up**: User creates account via `/api/users/signup`
2. **Sign In**: User authenticates via `/api/users/signin`
3. **JWT Token**: Backend returns JWT token
4. **Token Storage**: Frontend stores token in localStorage
5. **API Calls**: Frontend includes token in Authorization header

## 📡 API Endpoints

### Working Endpoints
- ✅ Authentication: `/api/users/signin`, `/api/users/signup`
- ✅ User Profile: `/api/users/profile`
- ✅ Books: `/api/books` (CRUD operations)
- ✅ Categories: `/api/categories`

### Recently Fixed Endpoints
- ✅ Cart: `/api/cart`, `/api/cart/books`
- ✅ Orders: `/api/orders`

### Missing Endpoints (TODO)
- ❌ Reviews: `/api/books/{id}/reviews`
- ❌ File Upload: `/api/upload`

## 🐛 Troubleshooting

### Common Issues

1. **Port Already in Use**
   ```bash
   # Check what's using the port
   netstat -ano | findstr :8081
   netstat -ano | findstr :3000
   ```

2. **Database Connection Error**
   - Ensure MySQL is running
   - Check credentials in `application.properties`
   - Verify database exists

3. **CORS Errors**
   - Backend must be running on port 8081
   - Frontend must be running on port 3000
   - Check browser console for CORS errors

4. **JWT Token Issues**
   - Check token expiration in `application.properties`
   - Verify token is being sent in Authorization header
   - Check browser localStorage for token

### Debug Commands

```bash
# Test backend health
curl http://localhost:8081/api/books

# Test frontend health
curl http://localhost:3000

# Test CORS
curl -H "Origin: http://localhost:3000" http://localhost:8081/api/books
```

## 📁 Project Structure

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
├── start-integration.bat  # Integration script
├── test-integration.bat   # Test script
└── INTEGRATION_GUIDE.md   # Detailed guide
```

## 🎯 Next Steps

1. **Complete Missing Endpoints**
   - Add review functionality
   - Add file upload capability
   - Add order details endpoint

2. **Enhancements**
   - Add error handling and user feedback
   - Implement real-time updates
   - Add pagination for large datasets
   - Add comprehensive logging

3. **Testing**
   - Add unit tests
   - Add integration tests
   - Add end-to-end tests

## 📞 Support

If you encounter issues:
1. Check the troubleshooting section above
2. Review the detailed `INTEGRATION_GUIDE.md`
3. Check browser console and backend logs
4. Verify all prerequisites are installed

## 🔄 Development Workflow

1. **Backend Changes**: Modify Spring Boot code
2. **Frontend Changes**: Modify React code
3. **Testing**: Use integration scripts
4. **Database**: Changes auto-applied via Hibernate
5. **Deployment**: Build and deploy both applications
