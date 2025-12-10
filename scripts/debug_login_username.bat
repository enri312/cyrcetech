@echo off
echo [DEBUG] Testing Login with USERNAME...
curl -v -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"admin\", \"password\":\"admin123\"}" 

echo.
echo.
echo [DEBUG] Testing Login with EMAIL (Legacy)...
curl -v -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"admin\", \"password\":\"admin123\"}" 
