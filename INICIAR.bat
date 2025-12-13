@echo off
cls

title Cyrcetech - Sistema de Gestion

echo.
echo ================================================================
echo              CYRCETECH - Sistema de Gestion
echo                   Iniciando Sistema...
echo ================================================================
echo.

REM ============================================
REM [1/4] Verificar Docker y PostgreSQL
REM ============================================
echo [1/4] Verificando Docker y Base de Datos...

docker info >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo       [X] Docker no esta corriendo
    echo       [-] Por favor, inicia Docker Desktop
    pause
    exit /b 1
)

docker ps -q -f name=cyrcetech_db | findstr . >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo       [!] Base de datos no esta corriendo
    echo       [-] Iniciando contenedor cyrcetech_db...
    docker start cyrcetech_db >nul 2>&1
    if %ERRORLEVEL% NEQ 0 (
        echo       [-] Creando contenedor nuevo...
        docker run -d --name cyrcetech_db -p 5433:5432 -e POSTGRES_USER=cyrcetech -e POSTGRES_PASSWORD=cyrcetech -e POSTGRES_DB=cyrcetech postgres:16 >nul 2>&1
    )
    echo       [-] Esperando 5 segundos...
    ping -n 6 127.0.0.1 >nul
    echo       [OK] Base de datos lista
) else (
    echo       [OK] Docker y base de datos funcionando
)

REM ============================================
REM [2/4] Verificar e Iniciar Backend (silencioso)
REM ============================================
echo [2/4] Verificando Backend...

netstat -ano | findstr ":8080" | findstr "LISTENING" >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo       [!] Backend no esta corriendo
    if exist "%~dp0backend\run_backend_debug.bat" (
        echo       [-] Iniciando backend en segundo plano...
        
        REM Iniciar backend de forma oculta usando PowerShell
        powershell -WindowStyle Hidden -Command "Start-Process -FilePath 'cmd.exe' -ArgumentList '/c cd /d \"%~dp0backend\" && gradlew.bat bootRun --console=plain' -WindowStyle Hidden"
        
        echo       [-] Esperando 25 segundos para que inicie...
        ping -n 26 127.0.0.1 >nul
        
        REM Verificar si inicio correctamente
        netstat -ano | findstr ":8080" | findstr "LISTENING" >nul 2>&1
        if %ERRORLEVEL% EQU 0 (
            echo       [OK] Backend iniciado correctamente
        ) else (
            echo       [!] Backend puede tardar mas, continuando...
        )
    ) else (
        echo       [X] No se encontro run_backend_debug.bat
        pause
        exit /b 1
    )
) else (
    echo       [OK] Backend ya esta corriendo
)

REM ============================================
REM [3/4] Compilar Cliente
REM ============================================
echo [3/4] Compilando Cliente de Escritorio...

if not exist "%~dp0gradlew.bat" (
    echo       [X] No se encontro gradlew.bat
    pause
    exit /b 1
)

call "%~dp0gradlew.bat" build -x test --console=plain --quiet
if %ERRORLEVEL% NEQ 0 (
    echo       [X] Error en la compilacion
    echo.
    call "%~dp0gradlew.bat" build -x test
    pause
    exit /b 1
)
echo       [OK] Compilacion exitosa

REM ============================================
REM [4/4] Iniciar Aplicacion
REM ============================================
echo [4/4] Iniciando Aplicacion...
echo.
echo ================================================================
echo         Abriendo Cyrcetech...
echo ================================================================
echo.

set JAVA_OPTS=--add-opens javafx.graphics/com.sun.glass.utils=ALL-UNNAMED --add-opens javafx.graphics/com.sun.marlin=ALL-UNNAMED --enable-native-access=javafx.graphics

call "%~dp0gradlew.bat" run --console=plain --quiet -Dorg.gradle.jvmargs="%JAVA_OPTS%"

echo.
echo ================================================================
echo               Aplicacion cerrada
echo ================================================================
pause
