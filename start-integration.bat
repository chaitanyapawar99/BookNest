@echo off
echo Starting Food Delivery Backend and Frontend Integration...

echo.
echo Starting Backend (Spring Boot) on port 8081...
start "Backend" cmd /k "cd /d %~dp0 && mvnw spring-boot:run"

echo.
echo Waiting for backend to start...
timeout /t 10 /nobreak > nul

echo.
echo Starting Frontend (React) on port 3000...
start "Frontend" cmd /k "cd /d %~dp0\booknest-frontend && npm start"

echo.
echo Integration started!
echo Backend: http://localhost:8081
echo Frontend: http://localhost:3000
echo.
echo Press any key to exit...
pause > nul
