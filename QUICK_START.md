# Quick Start Guide - Running the Grade Calculator

## Option 1: Install Maven via Chocolatey (Windows - Recommended)

If you have Chocolatey installed:

```powershell
choco install maven -y
```

Then run:
```powershell
cd "e:\Year 3\SPRING SEMESTER\Grade calculator"
mvn clean compile exec:java -Dexec.mainClass="com.example.gradcalc.console.ConsoleAppKt"
```

## Option 2: Download Maven Manually

1. Download Maven from: https://maven.apache.org/download.cgi
2. Extract to: `C:\software\apache-maven-3.9.x`
3. Add to System PATH:
   - Right-click "This PC" → Properties
   - Advanced system settings → Environment Variables
   - New variable: `MAVEN_HOME = C:\software\apache-maven-3.9.x`
   - Add to PATH: `%MAVEN_HOME%\bin`
4. Restart PowerShell and run:
```powershell
mvn --version  # verify installation
cd "e:\Year 3\SPRING SEMESTER\Grade calculator"
mvn clean compile exec:java -Dexec.mainClass="com.example.gradcalc.console.ConsoleAppKt"
```

## Option 3: Using Windows Package Manager (winget)

```powershell
winget install Maven
```

Then restart PowerShell and run the mvn command above.

## Option 4: Using WSL (Windows Subsystem for Linux)

```bash
wsl sudo apt update && wsl sudo apt install maven
wsl cd "e:\Year 3\SPRING SEMESTER\Grade calculator"
wsl mvn clean compile exec:java -Dexec.mainClass="com.example.gradcalc.console.ConsoleAppKt"
```

## Using the Batch Script

Once Maven is installed, simply double-click:
```
run-console.bat
```

## Console App Features

The console version includes:
- Interactive menu system
- Process mark sheet files (CSV or Excel)
- View sample data format
- View grading scale
- Display class statistics
- Save processed results to CSV

## Usage Example

1. Run the app: `mvn clean compile exec:java -Dexec.mainClass="com.example.gradcalc.console.ConsoleAppKt"`
2. Select option "1. Process mark sheet"
3. Enter: `sample_input.csv`
4. Results will be saved to `graded_sample_input.csv`
5. Statistics will display in the console

## Grading Scale

| Grade | Range     | Level      |
|-------|-----------|------------|
| A     | 70 - 100  | Excellent  |
| B     | 60 - 69   | Good       |
| C     | 50 - 59   | Satisfactory |
| D     | 45 - 49   | Acceptable |
| E     | 40 - 44   | Pass       |
| F     | 0 - 39    | Fail       |

## Generating API Documentation

After installation, generate Dokka documentation:

```powershell
mvn dokka:dokka
```

Then open `build/dokka/html/index.html` in your browser.
