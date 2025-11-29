@echo off
echo Iniciando aplicacion Cyrcetech...
echo.
call "%~dp0..\gradlew.bat" run --console=plain --no-daemon
pause
