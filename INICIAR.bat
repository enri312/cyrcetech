@echo off
cls
echo ========================================
echo   CYRCETECH - Sistema de Gestion
echo ========================================
echo.

REM Verificar que Ollama este corriendo
echo [1/4] Verificando Ollama...
curl -s http://localhost:11434/api/tags >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ADVERTENCIA: Ollama no esta corriendo
    echo Para usar la IA local, ejecuta: ollama serve
    echo.
    echo Presiona cualquier tecla para continuar sin IA...
    pause >nul
)

REM Verificar Backend (Puerto 8080)
echo [2/4] Verificando Backend...
netstat -ano | findstr :8080 >nul
if %ERRORLEVEL% NEQ 0 (
    echo Backend OFF. Iniciando servidor en segundo plano...
    start "Cyrcetech Backend" /min cmd /c "cd backend && ..\gradlew.bat bootRun"
    echo Esperando a que el backend inicie (por favor espere 20s)...
    timeout /t 20 >nul
) else (
    echo Backend ON.
)

echo [3/4] Compilando Cliente...
call "%~dp0gradlew.bat" build -x test --console=plain --quiet
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: La compilacion fallo
    echo Revisa los errores arriba
    pause
    exit /b 1
)

echo [4/4] Iniciando Aplicacion de Escritorio...
echo.

REM Configurar opciones de JVM para silenciar advertencias
set JAVA_OPTS=--add-opens javafx.graphics/com.sun.glass.utils=ALL-UNNAMED --add-opens javafx.graphics/com.sun.marlin=ALL-UNNAMED --enable-native-access=javafx.graphics

call "%~dp0gradlew.bat" run --console=plain --quiet -Dorg.gradle.jvmargs="%JAVA_OPTS%"

echo.
echo ========================================
echo Aplicacion cerrada
echo ========================================
pause
