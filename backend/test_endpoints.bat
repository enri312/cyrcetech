@echo off
echo Testing Customer API...

echo.
echo 1. Creating Customer...
curl -v -X POST http://localhost:8080/api/customers ^
  -H "Content-Type: application/json" ^
  -d "{\"name\": \"Juan Perez\", \"taxId\": \"20-12345678-9\", \"address\": \"Calle Falsa 123\", \"phone\": \"1122334455\"}" > create_response.json 2>&1
type create_response.json

echo.
echo.
echo 2. Listing Customers...
curl -v http://localhost:8080/api/customers > list_response.json 2>&1
type list_response.json

echo.
echo.
echo 3. Searching Customer...
curl -v "http://localhost:8080/api/customers/search?q=Juan" > search_response.json 2>&1
type search_response.json

echo.
echo Tests Completed.
