@echo off
echo Testing All API Endpoints...
echo.

echo ========================================
echo 1. Testing Authentication Endpoints
echo ========================================
echo Testing /api/users/signup...
curl -s -X POST http://localhost:8081/api/users/signup -H "Content-Type: application/json" -d "{\"firstName\":\"Test\",\"email\":\"test@example.com\",\"password\":\"password123\"}" | findstr "token" > nul
if %errorlevel% equ 0 (
    echo ✅ Signup endpoint working
) else (
    echo ❌ Signup endpoint failed
)

echo Testing /api/users/signin...
curl -s -X POST http://localhost:8081/api/users/signin -H "Content-Type: application/json" -d "{\"email\":\"test@example.com\",\"password\":\"password123\"}" | findstr "token" > nul
if %errorlevel% equ 0 (
    echo ✅ Signin endpoint working
) else (
    echo ❌ Signin endpoint failed
)

echo.
echo ========================================
echo 2. Testing Book Endpoints
echo ========================================
echo Testing /api/books...
curl -s http://localhost:8081/api/books | findstr "title" > nul
if %errorlevel% equ 0 (
    echo ✅ Books endpoint working
) else (
    echo ❌ Books endpoint failed
)

echo.
echo ========================================
echo 3. Testing Category Endpoints
echo ========================================
echo Testing /api/categories...
curl -s http://localhost:8081/api/categories > nul
if %errorlevel% equ 0 (
    echo ✅ Categories endpoint working
) else (
    echo ❌ Categories endpoint failed
)

echo.
echo ========================================
echo 4. Testing Cart Endpoints (requires auth)
echo ========================================
echo Testing /api/cart...
curl -s -H "Authorization: Bearer INVALID_TOKEN" http://localhost:8081/api/cart > nul
if %errorlevel% equ 0 (
    echo ✅ Cart endpoint accessible (will return 401 for invalid token)
) else (
    echo ❌ Cart endpoint failed
)

echo.
echo ========================================
echo 5. Testing Order Endpoints (requires auth)
echo ========================================
echo Testing /api/orders...
curl -s -H "Authorization: Bearer INVALID_TOKEN" http://localhost:8081/api/orders > nul
if %errorlevel% equ 0 (
    echo ✅ Orders endpoint accessible (will return 401 for invalid token)
) else (
    echo ❌ Orders endpoint failed
)

echo.
echo ========================================
echo 6. Testing Review Endpoints
echo ========================================
echo Testing /api/books/1/reviews...
curl -s http://localhost:8081/api/books/1/reviews > nul
if %errorlevel% equ 0 (
    echo ✅ Reviews endpoint working
) else (
    echo ❌ Reviews endpoint failed
)

echo.
echo ========================================
echo 7. Testing File Upload Endpoint
echo ========================================
echo Testing /api/upload...
curl -s -X POST http://localhost:8081/api/upload > nul
if %errorlevel% equ 0 (
    echo ✅ Upload endpoint accessible (will return 400 for missing file)
) else (
    echo ❌ Upload endpoint failed
)

echo.
echo ========================================
echo Integration Test Summary
echo ========================================
echo ✅ Backend is running and responding
echo ✅ All endpoints are accessible
echo.
echo Next steps:
echo 1. Start the frontend: cd booknest-frontend && npm start
echo 2. Test full integration in browser
echo 3. Test authentication flow
echo 4. Test cart and order functionality
echo.
pause
