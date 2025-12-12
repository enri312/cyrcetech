@echo off
setlocal
title Cyrcetech Launcher (Debug)
echo ========================================
echo   DEBUG MODE - SINGLE TERMINAL
echo ========================================
echo.

echo [1/3] Checking Backend (Port 8080)...
netstat -ano | findstr :8080 | findstr LISTEN >nul
if %ERRORLEVEL% EQU 0 (
    echo Backend is already running.
) else (
    echo Backend NOT detected. Starting in MINIMIZED window...
    
    REM Start backend in a MINIMIZED window to keep desktop clean
    start /MIN "Cyrcetech Backend" cmd /k "cd backend && ..\gradlew.bat bootRun"
    
    echo Waiting for Backend to initialize...
    
    REM Loop to wait for port 8080
    :WAIT_LOOP
    timeout /t 3 >nul
    netstat -ano | findstr :8080 | findstr LISTEN >nul
    if %ERRORLEVEL% NEQ 0 (
        echo ... waiting for backend ...
        goto WAIT_LOOP
    )
    echo Backend is UP!
)

echo.
echo [2/3] Building Client...
call "%~dp0gradlew.bat" build -x test > build_log.txt 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Build failed. See build_log.txt
    type build_log.txt
    pause
    exit /b 1
) else (
    echo Build SUCCESS.
)

echo.
echo [3/3] Running Client...
echo Launching Application...
call "%~dp0gradlew.bat" run --console=plain

echo.
echo Application Closed.
echo.
echo NOTE: The Backend might still be running in the background.
echo To stop it, close this window or use Task Manager.
pause
