@echo off
REM Grade Calculator - Run Console App
REM This batch file runs the Grade Calculator console application

cd /d "%~dp0"

echo.
echo ╔════════════════════════════════════════╗
echo ║      Grade Calculator - Console        ║
echo ╚════════════════════════════════════════╝
echo.

REM Check if Maven is installed locally
if exist "tools\apache-maven-3.9.6\bin\mvn.cmd" (
    echo Using local Maven installation...
    call "tools\apache-maven-3.9.6\bin\mvn.cmd" clean compile exec:java -Dexec.mainClass=com.example.gradcalc.console.ConsoleAppKt
) else (
    echo Maven not found. Please run setup-and-run.ps1 first.
    pause
)
