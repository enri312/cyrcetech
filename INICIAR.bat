@echo off
cls
echo ========================================
echo   CYRCETECH - Sistema de Gestion
echo ========================================
echo.

REM Verificar que Ollama este corriendo
echo [1/3] Verificando Ollama...
curl -s http://localhost:11434/api/tags >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ADVERTENCIA: Ollama no esta corriendo
    echo Para usar la IA local, ejecuta: ollama serve
    echo.
    echo Presiona cualquier tecla para continuar sin IA...
    pause >nul
)

echo [2/3] Compilando proyecto...
call "%~dp0gradlew.bat" build --console=plain --quiet
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: La compilacion fallo
    echo Revisa los errores arriba
    pause
    exit /b 1
)

echo [3/3] Iniciando aplicacion...
echo.
call "%~dp0gradlew.bat" run --console=plain --quiet

echo.
echo ========================================
echo Aplicacion cerrada
echo ========================================
pause
