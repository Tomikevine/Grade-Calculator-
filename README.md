# GradeFlow - Academic Grade Calculator
A Kotlin console application for processing academic mark sheets.

## Features
- Upload `.csv`, `.xlsx`, or `.xls` mark sheets
- Auto-calculate totals from CA + Exam scores when total is missing
- Assign letter grades
- Export graded mark sheet as CSV or Excel

## Grading Scale (default)
| Score | Grade |
|-------|-------|
| 70-100 | A |
| 60-69  | B |
| 50-59  | C |
| 45-49  | D |
| 40-44  | E |
| 0-39   | F |

## Quick Start
```bash
./gradlew run
```

Then:
1. Enter your file path (`.csv`, `.xlsx`, or `.xls`)
2. Choose output format
3. Enter output path (or press Enter to use default)

## Run With Your Own File Directly
Input only:
```bash
./gradlew run --args="path/to/input.csv"
```

Input and output:
```bash
./gradlew run --args="path/to/input.xlsx path/to/output_graded.xlsx"
```

## Build a Standalone JAR
```bash
./gradlew jar
java -jar build/libs/grade-calculator.jar path/to/input.csv path/to/output_graded.csv
```

## Run Tests
```bash
./gradlew test
```

## Generate Dokka Docs
```bash
./gradlew dokkaGeneratePublicationHtml
```

Or use:
```bash
./gradlew docs
```

Generated HTML documentation:
- `build/dokka/html/index.html`

## CSV Format
```csv
Name,Student ID,CA Score,Exam Score
Ada Okafor,CSC/2024/001,38,45
Bello Musa,CSC/2024/002,25,55
```

A sample file is included: `sample_marksheet.csv`.

## Architecture
```text
com.gradecalculator
  Main.kt

com.gradecalculator.domain
  GradeCalculator.kt

com.gradecalculator.data.model
  Models.kt

com.gradecalculator.data.parser
  ExcelParser.kt
```

See `docs/API_DOCUMENTATION.md` for detailed API documentation.
