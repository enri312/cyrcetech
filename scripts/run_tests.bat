@echo off
REM Unified Test Runner for Cyrcetech
REM Usage: run_tests.bat [pdf|n8n|manual|all]

if "%1"=="" goto help
if "%1"=="pdf" goto test_pdf
if "%1"=="n8n" goto test_n8n
if "%1"=="manual" goto manual_test
if "%1"=="all" goto test_all
goto help

:test_pdf
echo [RUNNER] Starting PDF Test...
call :login
if "%TOKEN%"=="" exit /b 1
echo [INFO] Creating Data for PDF...
curl -s -X POST http://localhost:8080/api/customers -H "Content-Type: application/json" -H "Authorization: Bearer %TOKEN%" -d "{\"name\":\"PDF Tester\",\"email\":\"pdf@cyrcetech.com\",\"phone\":\"555-PDF\",\"address\":\"Paper Lane\"}" > customer_pdf.json
for /f "tokens=*" %%a in ('powershell -Command "Get-Content customer_pdf.json | ConvertFrom-Json | Select-Object -ExpandProperty id"') do set CUST_ID=%%a
curl -s -X POST http://localhost:8080/api/equipment -H "Content-Type: application/json" -H "Authorization: Bearer %TOKEN%" -d "{\"customerId\":\"%CUST_ID%\",\"deviceType\":\"NOTEBOOK\",\"brand\":\"PDFBrand\",\"model\":\"PDF-Model\",\"serialNumber\":\"PDF-001\"}" > equipment_pdf.json
for /f "tokens=*" %%a in ('powershell -Command "Get-Content equipment_pdf.json | ConvertFrom-Json | Select-Object -ExpandProperty id"') do set EQUIP_ID=%%a
curl -s -X POST http://localhost:8080/api/tickets -H "Content-Type: application/json" -H "Authorization: Bearer %TOKEN%" -d "{\"customerId\":\"%CUST_ID%\",\"equipmentId\":\"%EQUIP_ID%\",\"problemDescription\":\"Prueba de PDF\",\"observations\":\"Debe generar documento.\"}" > ticket_created.json
for /f "tokens=*" %%a in ('powershell -Command "Get-Content ticket_created.json | ConvertFrom-Json | Select-Object -ExpandProperty id"') do set TICKET_ID=%%a
echo [INFO] Created Ticket: %TICKET_ID%
echo [INFO] Downloading PDF...
curl -s -o ticket_test.pdf -H "Authorization: Bearer %TOKEN%" http://localhost:8080/api/documents/ticket/%TICKET_ID%/pdf
echo [INFO] Download complete: ticket_test.pdf
call :cleanup
goto end

:test_n8n
echo [RUNNER] Starting n8n Webhook Test...
call :login
if "%TOKEN%"=="" exit /b 1
echo [INFO] Creating Data for n8n...
curl -s -X POST http://localhost:8080/api/customers -H "Content-Type: application/json" -H "Authorization: Bearer %TOKEN%" -d "{\"name\":\"N8N Tester\",\"email\":\"test@n8n.com\",\"phone\":\"555-0199\",\"address\":\"Docker Network\"}" > customer.json
for /f "tokens=*" %%a in ('powershell -Command "Get-Content customer.json | ConvertFrom-Json | Select-Object -ExpandProperty id"') do set CUST_ID=%%a
curl -s -X POST http://localhost:8080/api/equipment -H "Content-Type: application/json" -H "Authorization: Bearer %TOKEN%" -d "{\"customerId\":\"%CUST_ID%\",\"deviceType\":\"NOTEBOOK\",\"brand\":\"TestBrand\",\"model\":\"N8N-Model\",\"serialNumber\":\"WEBHOOK-001\"}" > equipment.json
for /f "tokens=*" %%a in ('powershell -Command "Get-Content equipment.json | ConvertFrom-Json | Select-Object -ExpandProperty id"') do set EQUIP_ID=%%a
echo [INFO] Creating Ticket to trigger Webhook...
curl -s -X POST http://localhost:8080/api/tickets -H "Content-Type: application/json" -H "Authorization: Bearer %TOKEN%" -d "{\"customerId\":\"%CUST_ID%\",\"equipmentId\":\"%EQUIP_ID%\",\"problemDescription\":\"WEBHOOK TRIGGER TEST\",\"observations\":\"This ticket should appear in n8n.\"}"
echo [INFO] Webhook trigger sent.
goto end

:manual_test
echo [RUNNER] Starting Manual Verification Test...
call :login
if "%TOKEN%"=="" exit /b 1
echo [STEP 2] Creating Customer...
curl -s -X POST http://localhost:8080/api/customers -H "Content-Type: application/json" -H "Authorization: Bearer %TOKEN%" -d "{\"name\":\"Manual Tester\",\"email\":\"man@test.com\",\"phone\":\"999-999\",\"address\":\"Manual Ln\"}" > cust_res.json
for /f "tokens=*" %%a in ('powershell -Command "Get-Content cust_res.json | ConvertFrom-Json | Select-Object -ExpandProperty id"') do set CUST_ID=%%a
echo [STEP 3] Creating Equipment...
curl -s -X POST http://localhost:8080/api/equipment -H "Content-Type: application/json" -H "Authorization: Bearer %TOKEN%" -d "{\"customerId\":\"%CUST_ID%\",\"deviceType\":\"NOTEBOOK\",\"brand\":\"ManBrand\",\"model\":\"ManModel\",\"serialNumber\":\"MAN-001\"}" > equip_res.json
for /f "tokens=*" %%a in ('powershell -Command "Get-Content equip_res.json | ConvertFrom-Json | Select-Object -ExpandProperty id"') do set EQUIP_ID=%%a
echo [STEP 4] Creating Ticket...
curl -s -X POST http://localhost:8080/api/tickets -H "Content-Type: application/json" -H "Authorization: Bearer %TOKEN%" -d "{\"customerId\":\"%CUST_ID%\",\"equipmentId\":\"%EQUIP_ID%\",\"problemDescription\":\"Manual Test\",\"observations\":\"Testing flow.\"}" > ticket_res.json
for /f "tokens=*" %%a in ('powershell -Command "Get-Content ticket_res.json | ConvertFrom-Json | Select-Object -ExpandProperty id"') do set TICKET_ID=%%a
echo [INFO] Ticket Created: %TICKET_ID%
echo [STEP 5] Cleanup...
call :cleanup
goto end

:test_all
call :test_pdf
call :test_n8n
goto end

:login
echo [LOGIN] Authenticating...
curl -s -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d "{\"username\":\"CENV\", \"password\":\"8994C\"}" > login_unified.json
for /f "tokens=*" %%a in ('powershell -Command "Get-Content login_unified.json | ConvertFrom-Json | Select-Object -ExpandProperty token"') do set TOKEN=%%a
if "%TOKEN%"=="" (
    echo [ERROR] Login failed.
    exit /b 1
)
echo [LOGIN] Success.
exit /b 0

:cleanup
if "%TICKET_ID%"=="" goto skip_ticket
curl -s -X DELETE -H "Authorization: Bearer %TOKEN%" http://localhost:8080/api/tickets/%TICKET_ID%
echo [CLEANUP] Deleted Ticket
:skip_ticket
if "%EQUIP_ID%"=="" goto skip_equip
curl -s -X DELETE -H "Authorization: Bearer %TOKEN%" http://localhost:8080/api/equipment/%EQUIP_ID%
echo [CLEANUP] Deleted Equipment
:skip_equip
if "%CUST_ID%"=="" goto skip_cust
curl -s -X DELETE -H "Authorization: Bearer %TOKEN%" http://localhost:8080/api/customers/%CUST_ID%
echo [CLEANUP] Deleted Customer
:skip_cust
exit /b 0

:help
echo Usage: run_tests.bat [pdf|n8n|manual|all]
echo Examples:
echo   run_tests.bat pdf    - Test PDF generation
echo   run_tests.bat n8n    - Test n8n Webhook
echo   run_tests.bat all    - Run all tests
:end
