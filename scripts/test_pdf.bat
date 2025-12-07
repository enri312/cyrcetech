@echo off
REM LOGIN
curl -s -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d "{\"email\":\"CENV\", \"password\":\"8994C\"}" > login_pdf.json
for /f "tokens=*" %%a in ('powershell -Command "Get-Content login_pdf.json | ConvertFrom-Json | Select-Object -ExpandProperty token"') do set TOKEN=%%a

REM CREATE DATA FOR PDF TEST
curl -s -X POST http://localhost:8080/api/customers -H "Content-Type: application/json" -H "Authorization: Bearer %TOKEN%" -d "{\"name\":\"PDF Tester\",\"email\":\"pdf@cyrcetech.com\",\"phone\":\"555-PDF\",\"address\":\"Paper Lane\"}" > customer_pdf.json
for /f "tokens=*" %%a in ('powershell -Command "Get-Content customer_pdf.json | ConvertFrom-Json | Select-Object -ExpandProperty id"') do set CUST_ID=%%a

curl -s -X POST http://localhost:8080/api/equipment -H "Content-Type: application/json" -H "Authorization: Bearer %TOKEN%" -d "{\"customerId\":\"%CUST_ID%\",\"deviceType\":\"NOTEBOOK\",\"brand\":\"PDFBrand\",\"model\":\"PDF-Model\",\"serialNumber\":\"PDF-001\"}" > equipment_pdf.json
for /f "tokens=*" %%a in ('powershell -Command "Get-Content equipment_pdf.json | ConvertFrom-Json | Select-Object -ExpandProperty id"') do set EQUIP_ID=%%a

curl -s -X POST http://localhost:8080/api/tickets -H "Content-Type: application/json" -H "Authorization: Bearer %TOKEN%" -d "{\"customerId\":\"%CUST_ID%\",\"equipmentId\":\"%EQUIP_ID%\",\"problemDescription\":\"Prueba de PDF\",\"observations\":\"Debe generar documento.\"}" > ticket_created.json
for /f "tokens=*" %%a in ('powershell -Command "Get-Content ticket_created.json | ConvertFrom-Json | Select-Object -ExpandProperty id"') do set TICKET_ID=%%a

echo [INFO] Created Ticket: %TICKET_ID%

echo [INFO] Downloading PDF for Ticket: %TICKET_ID%
curl -s -o ticket_test.pdf -H "Authorization: Bearer %TOKEN%" http://localhost:8080/api/documents/ticket/%TICKET_ID%/pdf
echo [INFO] Download complete: ticket_test.pdf. Opening file...

REM CLEANUP
echo [INFO] Cleaning up test data...
curl -s -X DELETE -H "Authorization: Bearer %TOKEN%" http://localhost:8080/api/tickets/%TICKET_ID%
echo [INFO] Deleted Ticket %TICKET_ID%

curl -s -X DELETE -H "Authorization: Bearer %TOKEN%" http://localhost:8080/api/equipment/%EQUIP_ID%
echo [INFO] Deleted Equipment %EQUIP_ID%

curl -s -X DELETE -H "Authorization: Bearer %TOKEN%" http://localhost:8080/api/customers/%CUST_ID%
echo [INFO] Deleted Customer %CUST_ID%

echo [INFO] Test Complete.
start ticket_test.pdf
