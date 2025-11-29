@echo off
echo Deleting old directories...
if exist src\main\java\com\cyrcetech\model rmdir /S /Q src\main\java\com\cyrcetech\model
if exist src\main\java\com\cyrcetech\service rmdir /S /Q src\main\java\com\cyrcetech\service
if exist src\main\java\com\cyrcetech\controller rmdir /S /Q src\main\java\com\cyrcetech\controller
if exist src\main\java\com\cyrcetech\CyrcetechApp.java del /Q src\main\java\com\cyrcetech\CyrcetechApp.java
echo Cleanup complete!
