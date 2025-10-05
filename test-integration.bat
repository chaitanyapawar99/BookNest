@echo off
echo Testing Frontend-Backend Integration...
echo.

echo 1. Testing Backend Health...
curl -s http://localhost:8081/api/books > nul
if %errorlevel% equ 0 (
    echo ✅ Backend is running on port 8081
) else (
    echo ❌ Backend is not responding on port 8081
    echo Please start the backend first: mvnw spring-boot:run
    pause
    exit /b 1
)

echo.
echo 2. Testing Frontend Health...
curl -s http://localhost:3000 > nul
if %errorlevel% equ 0 (
    echo ✅ Frontend is running on port 3000
) else (
    echo ❌ Frontend is not responding on port 3000
    echo Please start the frontend first: cd booknest-frontend && npm start
    pause
    exit /b 1
)

echo.
echo 3. Testing CORS Configuration...
curl -s -H "Origin: http://localhost:3000" http://localhost:8081/api/books > nul
if %errorlevel% equ 0 (
    echo ✅ CORS is properly configured
) else (
    echo ❌ CORS configuration issue
)

echo.
echo 4. Testing API Endpoints...
echo Testing /api/books endpoint...
curl -s http://localhost:8081/api/books | findstr "title" > nul
if %errorlevel% equ 0 (
    echo ✅ Books API is working
) else (
    echo ❌ Books API is not working properly
)

echo.
echo Integration Test Complete!
echo.
echo Next steps:
echo 1. Open http://localhost:3000 in your browser
echo 2. Try to sign up or sign in
echo 3. Browse books and test the cart functionality
echo.
pause
