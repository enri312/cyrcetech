@echo off
REM LOGIN
curl -s -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d "{\"email\":\"CENV\", \"password\":\"8994C\"}" > login.json
for /f "tokens=*" %%a in ('powershell -Command "Get-Content login.json | ConvertFrom-Json | Select-Object -ExpandProperty token"') do set TOKEN=%%a

echo [INFO] Authenticated. Token captured.

REM CREATE CUSTOMER
curl -s -X POST http://localhost:8080/api/customers -H "Content-Type: application/json" -H "Authorization: Bearer %TOKEN%" -d "{\"name\":\"N8N Tester\",\"email\":\"test@n8n.com\",\"phone\":\"555-0199\",\"address\":\"Docker Network\"}" > customer.json
for /f "tokens=*" %%a in ('powershell -Command "Get-Content customer.json | ConvertFrom-Json | Select-Object -ExpandProperty id"') do set CUST_ID=%%a
echo [INFO] Customer Created: %CUST_ID%

REM CREATE EQUIPMENT
curl -s -X POST http://localhost:8080/api/equipment -H "Content-Type: application/json" -H "Authorization: Bearer %TOKEN%" -d "{\"customerId\":\"%CUST_ID%\",\"deviceType\":\"NOTEBOOK\",\"brand\":\"TestBrand\",\"model\":\"N8N-Model\",\"serialNumber\":\"WEBHOOK-001\"}" > equipment.json
for /f "tokens=*" %%a in ('powershell -Command "Get-Content equipment.json | ConvertFrom-Json | Select-Object -ExpandProperty id"') do set EQUIP_ID=%%a
echo [INFO] Equipment Created: %EQUIP_ID%

REM CREATE TICKET
echo [INFO] Creating Ticket to trigger Webhook...
curl -s -X POST http://localhost:8080/api/tickets -H "Content-Type: application/json" -H "Authorization: Bearer %TOKEN%" -d "{\"customerId\":\"%CUST_ID%\",\"equipmentId\":\"%EQUIP_ID%\",\"problemDescription\":\"WEBHOOK TRIGGER TEST\",\"observations\":\"This ticket should appear in n8n.\"}"
echo.
echo [INFO] Done.
