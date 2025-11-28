@echo off
echo Starting build... > build_output.txt
call C:\Gradle\gradle-9.2.1\bin\gradle.bat run >> build_output.txt 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo Build failed with error level %ERRORLEVEL% >> build_output.txt
) else (
    echo Build success >> build_output.txt
)
echo Done. >> build_output.txt
