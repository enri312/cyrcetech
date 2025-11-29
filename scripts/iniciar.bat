@echo off
cls
echo ========================================
echo   INICIANDO CYRCETECH
echo ========================================
echo.
echo Verificando Java...
java --version
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java no esta instalado o no esta en el PATH
    pause
    exit /b 1
)

echo.
echo Limpiando proyecto anterior...
call "%~dp0..\gradlew.bat" clean

echo.
echo Compilando proyecto...
call "%~dp0..\gradlew.bat" build
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: La compilacion fallo
    pause
    exit /b 1
)

echo.
echo Iniciando aplicacion...
call "%~dp0..\gradlew.bat" run --console=plain --no-daemon

echo.
echo ========================================
echo Proceso finalizado
echo ========================================
pause
