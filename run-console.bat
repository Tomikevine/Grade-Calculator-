@echo off
REM Grade Calculator - Setup and Run Script
REM This script installs Maven (if needed) and runs the Grade Calculator

echo.
echo ╔════════════════════════════════════════╗
echo ║  Grade Calculator - Setup & Run        ║
echo ╚════════════════════════════════════════╝
echo.

REM Check if Maven is installed
mvn --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ⚠️  Maven is not installed.
    echo.
    echo To use this script, install Maven:
    echo.
    echo Option 1: Using Chocolatey (Windows package manager)
    echo   - First install Chocolatey from https://chocolatey.org/install
    echo   - Then run: choco install maven
    echo.
    echo Option 2: Manual Download
    echo   - Download from https://maven.apache.org/download.cgi
    echo   - Extract to a folder
    echo   - Add bin folder to system PATH
    echo.
    echo Option 3: Using WSL (Windows Subsystem for Linux)
    echo   - Run: wsl sudo apt update ^&^& wsl sudo apt install maven
    echo.
    pause
    exit /b 1
)

echo ✓ Maven found
echo.
echo Running Grade Calculator Console...
echo.

mvn clean compile exec:java -Dexec.mainClass="com.example.gradcalc.console.ConsoleAppKt"

pause
