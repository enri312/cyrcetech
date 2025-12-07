@echo off
echo [STEP 1] Logging in...
curl -v -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d "{\"email\":\"CENV\", \"password\":\"8994C\"}" > login_res.json 2> login_err.txt
for /f "tokens=*" %%a in ('powershell -Command "Get-Content login_res.json | ConvertFrom-Json | Select-Object -ExpandProperty token"') do set TOKEN=%%a

if "%TOKEN%"=="" (
    echo [ERROR] Authentication failed. Check login_err.txt.
    type login_err.txt
    exit /b
)
echo [INFO] Token obtained.

echo [STEP 2] Creating Customer...
curl -X POST http://localhost:8080/api/customers -H "Content-Type: application/json" -H "Authorization: Bearer %TOKEN%" -d "{\"name\":\"PDF Manual Tester\",\"email\":\"manual@pdf.com\",\"phone\":\"999-999\",\"address\":\"Manual Lane\"}" > cust_res.json
for /f "tokens=*" %%a in ('powershell -Command "Get-Content cust_res.json | ConvertFrom-Json | Select-Object -ExpandProperty id"') do set CUST_ID=%%a
echo [INFO] Customer ID: %CUST_ID%

echo [STEP 3] Creating Equipment...
curl -X POST http://localhost:8080/api/equipment -H "Content-Type: application/json" -H "Authorization: Bearer %TOKEN%" -d "{\"customerId\":\"%CUST_ID%\",\"deviceType\":\"NOTEBOOK\",\"brand\":\"ManualBrand\",\"model\":\"Manual-Model\",\"serialNumber\":\"MANUAL-001\"}" > equip_res.json
for /f "tokens=*" %%a in ('powershell -Command "Get-Content equip_res.json | ConvertFrom-Json | Select-Object -ExpandProperty id"') do set EQUIP_ID=%%a
echo [INFO] Equipment ID: %EQUIP_ID%

echo [STEP 4] Creating Ticket...
curl -X POST http://localhost:8080/api/tickets -H "Content-Type: application/json" -H "Authorization: Bearer %TOKEN%" -d "{\"customerId\":\"%CUST_ID%\",\"equipmentId\":\"%EQUIP_ID%\",\"problemDescription\":\"Manual PDF Test\",\"observations\":\"Testing PDF generation manually.\"}" > ticket_res.json
for /f "tokens=*" %%a in ('powershell -Command "Get-Content ticket_res.json | ConvertFrom-Json | Select-Object -ExpandProperty id"') do set TICKET_ID=%%a
echo [INFO] Ticket ID: %TICKET_ID%

echo [STEP 5] Download PDF...
curl -v -o manual_ticket.pdf -H "Authorization: Bearer %TOKEN%" http://localhost:8080/api/documents/ticket/%TICKET_ID%/pdf
echo [INFO] PDF Downloaded to manual_ticket.pdf

echo [STEP 6] Cleanup...
curl -X DELETE -H "Authorization: Bearer %TOKEN%" http://localhost:8080/api/tickets/%TICKET_ID%
echo [INFO] Deleted Ticket
curl -X DELETE -H "Authorization: Bearer %TOKEN%" http://localhost:8080/api/equipment/%EQUIP_ID%
echo [INFO] Deleted Equipment
curl -X DELETE -H "Authorization: Bearer %TOKEN%" http://localhost:8080/api/customers/%CUST_ID%
echo [INFO] Deleted Customer

echo [SUCCESS] Script finished.
