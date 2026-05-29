@echo off
REM ============================================
REM 1982 - Build and Run (Windows)
REM ============================================
REM Requiere: Java 8+ instalado (java y javac en PATH)
REM Uso:      run.bat

set PROJECT_DIR=%~dp0
set SRC_DIR=%PROJECT_DIR%src\main\java
set LIB_DIR=%PROJECT_DIR%lib
set OUT_DIR=%PROJECT_DIR%out
set RESOURCES_DIR=%PROJECT_DIR%src\main\Resources

echo === 1982 - Compilando... ===

REM Limpiar y crear directorio de salida
if exist "%OUT_DIR%" rmdir /s /q "%OUT_DIR%"
mkdir "%OUT_DIR%"

REM Copiar recursos al directorio de salida
xcopy /s /e /q /y "%RESOURCES_DIR%\*" "%OUT_DIR%\" >nul 2>&1

REM Compilar todos los .java
dir /s /b "%SRC_DIR%\*.java" > "%OUT_DIR%\sources.txt"
javac -cp "%LIB_DIR%\core.jar" -d "%OUT_DIR%" @"%OUT_DIR%\sources.txt"
del "%OUT_DIR%\sources.txt"

echo === Compilacion exitosa ===
echo === Ejecutando 1982... ===

REM Ejecutar
cd /d "%OUT_DIR%"
java -cp ".;%LIB_DIR%\core.jar" Home.GameApp
