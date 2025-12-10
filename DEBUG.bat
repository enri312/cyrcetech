@echo off
echo ========================================
echo   DEBUG MODE - CYRCETECH
echo ========================================
echo.

echo [1/3] Backend Check...
netstat -ano | findstr :8080
if %ERRORLEVEL% EQU 0 (
    echo Backend FOUND on port 8080.
) else (
    echo Backend NOT FOUND. Attempting start...
    start /min cmd /c "cd backend && ..\gradlew.bat bootRun"
    timeout /t 10
)

echo [2/3] Building Client (capturing output)...
call "%~dp0gradlew.bat" build -x test > build_log.txt 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Build failed. See build_log.txt
    type build_log.txt
    pause
    exit /b 1
) else (
    echo Build SUCCESS.
)

echo [3/3] Running Client (Forwarding Output)...
echo.
echo Launching... please wait. Window should appear.
echo.

call "%~dp0gradlew.bat" run --info

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Run command failed with code %ERRORLEVEL%
    pause
)

echo.
echo Application Closed.
pause
