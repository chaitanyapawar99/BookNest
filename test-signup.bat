@echo off
echo Testing Signup Functionality...
echo.

echo ========================================
echo 1. Testing Backend Signup Endpoint
echo ========================================
echo Testing /api/users/signup...

curl -s -X POST http://localhost:8081/api/users/signup ^
  -H "Content-Type: application/json" ^
  -d "{\"firstName\":\"Test\",\"lastName\":\"TestUser\",\"email\":\"testuser@example.com\",\"password\":\"test@123\",\"userRole\":\"USER\",\"dob\":\"1990-01-01\"}" ^
  -w "\nHTTP Status: %%{http_code}\n"

echo.
echo ========================================
echo 2. Testing Signin After Signup
echo ========================================
echo Testing /api/users/signin...

curl -s -X POST http://localhost:8081/api/users/signin ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"testuser@example.com\",\"password\":\"test@123\"}" ^
  -w "\nHTTP Status: %%{http_code}\n"

echo.
echo ========================================
echo Signup Test Complete!
echo ========================================
echo.
echo If you see HTTP Status: 201 for signup and 200 for signin, the backend is working.
echo If you see errors, check the backend logs for more details.
echo.
pause
