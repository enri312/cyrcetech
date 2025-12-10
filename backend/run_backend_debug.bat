@echo off
echo Starting Cyrcetech Backend...
cd /d "%~dp0"
echo Working directory: %CD%
call gradlew.bat bootRun
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Backend crashed!
    echo.
)
pause
