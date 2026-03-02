# GradeFlow API Documentation
## Dokka Reference (v1.0.0)

## Overview
GradeFlow is a Kotlin console application that:
1. Reads a mark sheet from `.csv`, `.xlsx`, or `.xls`.
2. Computes or uses each student's `totalScore`.
3. Assigns letter grades with a fixed scale.
4. Writes graded output to `.csv`, `.xlsx`, or `.xls`.

This document describes the current implementation in this repository.

## Current Package Structure
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

## Public API Reference
### Package `com.gradecalculator`
#### `fun main(args: Array<String>)`
Application entry point.

Behavior:
1. Accepts optional CLI args:
   - `args[0]`: input file path
   - `args[1]`: output file path
2. If args are missing, prompts user interactively.
3. Validates input file existence.
4. Parses file with `ExcelParser.readFile`.
5. Grades each row with `GradeCalculator.calculateGrade`.
6. Exports result with `ExcelParser.writeFile`.

Supported input extensions:
- `.csv`
- `.xlsx`
- `.xls`

Supported output extensions:
- `.csv`
- `.xlsx`
- `.xls`

### Package `com.gradecalculator.domain`
#### `class GradeCalculator`
Stateless grade-assignment utility.

#### `fun calculateGrade(mark: Double?): String`
Maps a numeric mark to a letter grade:

| Range | Grade |
|---|---|
| 70+ | A |
| 60-69 | B |
| 50-59 | C |
| 45-49 | D |
| 40-44 | E |
| 0-39 | F |

Returns `"Invalid"` when:
- `mark == null`
- `mark < 0`

### Package `com.gradecalculator.data.model`
#### `data class Student`
Represents one student row in parsed/exported files.

```kotlin
data class Student(
    val name: String,
    val studentId: String,
    val caScore: Double,
    val examScore: Double,
    val totalScore: Double,
    var grade: String? = null
)
```

Field notes:
- `totalScore` is used for grade computation.
- `grade` is assigned after processing.

### Package `com.gradecalculator.data.parser`
#### `class ExcelParser`
Handles both parse and export for CSV and Excel formats.

#### `fun readFile(filePath: String): List<Student>`
Dispatches by extension:
- `.csv` -> CSV parser
- `.xlsx` / `.xls` -> Excel parser

Throws `IllegalArgumentException` for unsupported extensions.

Header alias support (case-insensitive):

| Logical field | Accepted headers |
|---|---|
| Name | `name`, `student name`, `full name`, `student` |
| Student ID | `student id`, `id`, `matricule`, `matric`, `matric no`, `reg no` |
| CA Score | `ca score`, `ca`, `continuous assessment`, `ca/test` |
| Exam Score | `exam score`, `exam`, `examination`, `exam mark` |
| Total Score | `total score`, `total`, `score`, `mark`, `overall` |

Row behavior:
- Empty `name` + empty `studentId` rows are skipped.
- If `totalScore` is missing/blank, it is derived as `caScore + examScore`.
- Non-numeric score values default to `0.0`.

#### `fun writeFile(filePath: String, students: List<Student>)`
Dispatches by extension:
- `.csv` -> CSV output
- `.xlsx` -> Office Open XML output
- `.xls` -> legacy Excel output

Throws `IllegalArgumentException` for unsupported extensions.

Output columns:
1. Name
2. Student ID
3. CA Score
4. Exam Score
5. Total Score
6. Grade

## CLI Usage
### Interactive
```bash
./gradlew run
```

### Non-interactive
```bash
./gradlew run --args="path/to/input.csv path/to/output_graded.csv"
```

### Fat JAR
```bash
./gradlew jar
java -jar build/libs/grade-calculator.jar path/to/input.xlsx path/to/output_graded.xlsx
```

## Dokka Generation
Dokka plugin is configured in `build.gradle.kts`.

Generate HTML docs:
```bash
./gradlew dokkaGeneratePublicationHtml
```

Or use the alias task:
```bash
./gradlew docs
```

Default output:
- `build/dokka/html`

## Error Handling Notes
- Missing input path -> app prints message and exits.
- Missing/non-existent input file -> app prints message and exits.
- Unsupported extension -> app prints parser error message.
- Empty parsed result -> app prints "No student rows were found in the uploaded file."

## Implementation Notes
- This repository currently uses a single parser class (`ExcelParser`) for both CSV and Excel I/O.
- Historical references to `ConsoleUI`, `MarkSheetProcessor`, and repository abstractions are not part of the current codebase.
