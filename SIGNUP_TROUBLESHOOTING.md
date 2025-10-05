# Signup Troubleshooting Guide

## Issues Fixed

### 1. **Field Mapping Issues**
- **Problem**: Frontend was sending `role` but backend expects `userRole`
- **Solution**: Updated frontend to send `userRole` field

### 2. **Missing Required Fields**
- **Problem**: Frontend wasn't sending `dob` (date of birth) field
- **Solution**: Added date of birth field to signup form

### 3. **Password Validation Mismatch**
- **Problem**: Backend has strict password requirements that frontend didn't match
- **Backend Requirements**:
  - At least one digit (`(?=.*\\d)`)
  - At least one lowercase letter (`(?=.*[a-z])`)
  - At least one special character from `#@$*` (`(?=.*[#@$*])`)
  - Length between 5-20 characters
- **Solution**: Updated frontend validation to match backend requirements

### 4. **Last Name Validation Mismatch**
- **Problem**: Backend requires lastName to be 4-20 characters, frontend only required 2
- **Solution**: Updated frontend validation to match backend requirements

## Testing the Fix

### 1. Test Backend Signup
```bash
# Run the signup test
test-signup.bat
```

### 2. Test Frontend Signup
1. Start both backend and frontend:
   ```bash
   start-integration.bat
   ```

2. Navigate to http://localhost:3000/signup

3. Fill out the form with valid data:
   - **First Name**: Test
   - **Last Name**: TestUser (must be 4+ characters)
   - **Email**: test@example.com
   - **Password**: test@123 (must contain digit, lowercase, and special char)
   - **Date of Birth**: Select any past date
   - **Account Type**: User

4. Submit the form

## Common Issues and Solutions

### 1. **"Invalid password format" Error**
- **Cause**: Password doesn't meet backend requirements
- **Solution**: Use password like `test@123` (contains digit, lowercase, special char)

### 2. **"Invalid last name length" Error**
- **Cause**: Last name is less than 4 characters
- **Solution**: Use last name with 4+ characters

### 3. **"Dob must be in past" Error**
- **Cause**: Date of birth is in the future
- **Solution**: Select a past date

### 4. **"Duplicate email detected" Error**
- **Cause**: Email already exists in database
- **Solution**: Use a different email address

### 5. **CORS Errors**
- **Cause**: Frontend can't reach backend
- **Solution**: Ensure backend is running on port 8081 and CORS is configured

## Debug Steps

### 1. Check Backend Logs
Look for these log messages:
```
Signup attempt for: test@example.com with role: CUSTOMER
Signup successful for: test@example.com
```

### 2. Check Browser Console
- Open Developer Tools (F12)
- Go to Console tab
- Look for any error messages

### 3. Check Network Tab
- Open Developer Tools (F12)
- Go to Network tab
- Try to signup and check the request/response

### 4. Test Backend Directly
```bash
curl -X POST http://localhost:8081/api/users/signup \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Test",
    "lastName": "TestUser",
    "email": "test@example.com",
    "password": "test@123",
    "userRole": "CUSTOMER",
    "dob": "1990-01-01"
  }'
```

## Valid Test Data

### Valid Signup Data
```json
{
  "firstName": "Test",
  "lastName": "TestUser",
  "email": "test@example.com",
  "password": "test@123",
  "userRole": "USER",
  "dob": "1990-01-01"
}
```

### Valid Login Data
```json
{
  "email": "test@example.com",
  "password": "test@123"
}
```

## Expected Response

### Successful Signup Response
```json
{
  "id": 1,
  "firstName": "Test",
  "lastName": "TestUser",
  "email": "test@example.com",
  "dob": "1990-01-01",
  "userRole": "USER"
}
```

### Successful Login Response
```json
{
  "message": "Successful login!",
  "jwt": "eyJhbGciOiJIUzI1NiJ9..."
}
```

## If Issues Persist

1. **Check Database**: Ensure MySQL is running and accessible
2. **Check Ports**: Ensure backend is on 8081 and frontend on 3000
3. **Clear Browser Cache**: Clear localStorage and cookies
4. **Restart Services**: Restart both backend and frontend
5. **Check Logs**: Look at backend console for detailed error messages
