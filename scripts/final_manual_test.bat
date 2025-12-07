@echo off
set TOKEN=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJDRU5WIiwiaWF0IjoxNzY1MDgwMzcwLCJleHAiOjE3NjUxNjY3NzB9.FUv81Gv5w5asx6pQog8XKMCvXEPgW8X6-HjCEZwoxfU
set CUST_ID=7df3e4ec-b354-4205-8363-115af69d609a

echo [1] Creating Equipment...
curl -s -X POST http://localhost:8080/api/equipment -H "Content-Type: application/json" -H "Authorization: Bearer %TOKEN%" -d "{\"customerId\":\"%CUST_ID%\",\"deviceType\":\"NOTEBOOK\",\"brand\":\"FinalBrand\",\"model\":\"Final-Model\",\"serialNumber\":\"FINAL-001\"}" > final_equip.json
for /f "tokens=*" %%a in ('powershell -Command "Get-Content final_equip.json | ConvertFrom-Json | Select-Object -ExpandProperty id"') do set EQUIP_ID=%%a
echo [INFO] Equipment: %EQUIP_ID%

echo [2] Creating Ticket...
curl -s -X POST http://localhost:8080/api/tickets -H "Content-Type: application/json" -H "Authorization: Bearer %TOKEN%" -d "{\"customerId\":\"%CUST_ID%\",\"equipmentId\":\"%EQUIP_ID%\",\"problemDescription\":\"Final PDF Test\",\"observations\":\"Works?\"}" > final_ticket.json
for /f "tokens=*" %%a in ('powershell -Command "Get-Content final_ticket.json | ConvertFrom-Json | Select-Object -ExpandProperty id"') do set TICKET_ID=%%a
echo [INFO] Ticket: %TICKET_ID%

echo [3] Downloading PDF...
curl -s -o final_ticket.pdf -H "Authorization: Bearer %TOKEN%" http://localhost:8080/api/documents/ticket/%TICKET_ID%/pdf
echo [INFO] Downloaded final_ticket.pdf

echo [4] Deleting Ticket...
curl -s -X DELETE -H "Authorization: Bearer %TOKEN%" http://localhost:8080/api/tickets/%TICKET_ID% 
echo.

echo [5] Deleting Equipment...
curl -s -X DELETE -H "Authorization: Bearer %TOKEN%" http://localhost:8080/api/equipment/%EQUIP_ID%
echo.

echo [INFO] Done.
