# Grade Calculator

A Kotlin Compose for Desktop application that allows lecturers to upload a mark sheet file (CSV or Excel), calculates grades based on configurable rules, and outputs a new CSV file with computed grades. It also displays summary statistics.

## Features

- Upload mark sheet CSV or Excel file
- Parse student data (Name, ID, CA score, Exam score, Total)
- Compute total score if necessary
- Assign grades using default scale (A-F)
- Generate new CSV output with grade column
- Show class average, highest, lowest, and grade distribution
- Comprehensive API documentation via Dokka

## Getting Started

### Prerequisites

- Java JDK 17 or higher installed
- Maven 3.6+ installed

### Running the Application

1. Open a terminal in the project root.
2. Run the application using Maven:

```bash
mvn clean compile exec:java -Dexec.mainClass="com.example.gradcalc.MainKt"
```

The first run will download dependencies; subsequent runs are faster.

3. The Compose for Desktop window will open. Use **Choose File** to select your mark sheet (CSV or Excel).
4. Click **Process** to compute grades and generate output file (`graded_<originalname>.csv`).
5. View statistics in the UI and check the generated file.

### Sample Input

A sample CSV (`sample_input.csv`) is included in the project root:

```csv
Student Name,Matric Number,CA,Exam
Alice,12345,30,45
Bob,23456,25,30
Charlie,34567,40,35
Daisy,45678,20,15
Eve,56789,15,25
```

### Output Example

After processing, a file named `graded_sample_input.csv` will appear with computed grades.

## Code Structure

- `model` package: `Student` data class with properties and methods
- `domain` package: `GradeCalculator` business logic with grading rules
- `data` package: `FileHandler` for parsing and writing CSV/Excel files
- `utils` package: `Statistics` for class performance analysis
- `Main.kt`: Compose for Desktop UI and application entry point

## API Documentation

Comprehensive KDoc documentation is included for all public classes and functions.

### Generating Documentation

#### HTML Documentation
```bash
mvn dokka:dokka
```
Output: `build/dokka/html/index.html`

#### Markdown Documentation
```bash
mvn dokka:dokka@dokkaMarkdown
```
Output: `build/dokka/markdown/`

#### View HTML Documentation
Open `build/dokka/html/index.html` in your web browser to view interactive documentation.

See [DOKKA_GUIDE.md](DOKKA_GUIDE.md) for detailed documentation generation and customization instructions.

## Project Documentation

The project includes comprehensive KDoc comments:

- **Student**: Data model with 6 properties and computeTotal() method
- **GradeCalculator**: Singleton with assignGrade(student) method
  - Grading scale: A (70-100), B (60-69), C (50-59), D (45-49), E (40-44), F (<40)
  - Validates scores are within 0-100 range
- **FileHandler**: Supports CSV and Excel parsing with flexible headers
  - Methods: parse(), parseCsv(), parseExcel(), parseRowMap(), writeCsv()
- **Statistics**: Class performance metrics
  - Methods: average(), highest(), lowest(), gradeDistribution()

## Extending the App

- Modify grading rules in `GradeCalculator` or make them configurable
- Add support for additional file formats or output types
- Enhance UI with charts or detailed error reporting
- Customize Dokka output in `pom.xml` and `build.gradle.kts`

## Notes

- Validates score ranges (0-100); invalid totals yield "Invalid" grade
- Handles missing or malformed data gracefully by skipping bad rows
- Flexible header matching supports various column name conventions
- All public APIs are fully documented with KDoc comments
