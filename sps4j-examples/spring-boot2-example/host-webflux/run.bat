@echo off

for /f "tokens=*" %%i in ('mvn help:evaluate "-Dexpression=revision" -q -DforceStdout') do set VERSION=%%i
set SCRIPT_DIR=%~dp0
set DIST_DIR=%SCRIPT_DIR%dist
for %%i in ("%SCRIPT_DIR%.") do set PROJECT_NAME=%%~nxi

java -cp "%DIST_DIR%\%PROJECT_NAME%-%VERSION%.jar;%DIST_DIR%\lib\*;%DIST_DIR%" io.github.sps4j.example.webflux.host.Main %*
