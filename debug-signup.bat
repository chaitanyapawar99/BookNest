@echo off
echo Debugging Signup Endpoint...
echo.

echo ========================================
echo 1. Testing with curl -v (verbose)
echo ========================================
echo Testing /api/users/signup with verbose output...

curl -v -X POST http://localhost:8081/api/users/signup ^
  -H "Content-Type: application/json" ^
  -d "{\"firstName\":\"Test\",\"lastName\":\"TestUser\",\"email\":\"debug@example.com\",\"password\":\"test@123\",\"userRole\":\"USER\",\"dob\":\"1990-01-01\"}"

echo.
echo ========================================
echo 2. Testing with different date format
echo ========================================
echo Testing with ISO date format...

curl -v -X POST http://localhost:8081/api/users/signup ^
  -H "Content-Type: application/json" ^
  -d "{\"firstName\":\"Test\",\"lastName\":\"TestUser\",\"email\":\"debug2@example.com\",\"password\":\"test@123\",\"userRole\":\"USER\",\"dob\":\"1990-01-01T00:00:00.000Z\"}"

echo.
echo ========================================
echo 3. Testing with minimal data
echo ========================================
echo Testing with minimal required fields...

curl -v -X POST http://localhost:8081/api/users/signup ^
  -H "Content-Type: application/json" ^
  -d "{\"firstName\":\"Test\",\"lastName\":\"TestUser\",\"email\":\"debug3@example.com\",\"password\":\"test@123\",\"userRole\":\"USER\",\"dob\":\"1990-01-01\"}"

echo.
echo ========================================
echo Debug Complete!
echo ========================================
echo.
echo Check the output above for any error messages.
echo Look for:
echo - HTTP status codes
echo - Response headers
echo - Response body
echo - Any error messages
echo.
pause
