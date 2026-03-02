# Dokka Documentation Guide

This guide explains how to generate and view API documentation for the Grade Calculator project using Dokka.

## Overview

Dokka is a documentation engine for Kotlin that generates beautiful HTML and Markdown documentation from KDoc comments. This project is configured to generate documentation in two formats:
- **HTML**: Interactive web-based documentation
- **Markdown (GFM)**: GitHub-Flavored Markdown for integration with repositories

## Prerequisites

- Maven 3.6+ installed
- Java 17+ installed
- The project built with Maven at least once

## Generating Documentation

### HTML Documentation

Generate interactive HTML documentation using the default Dokka task:

```bash
mvn dokka:dokka
```

This command:
1. Parses all Kotlin source files
2. Extracts KDoc comments
3. Generates HTML documentation
4. Places output in `build/dokka/html/`

**Time**: First run takes 30-60 seconds (depends on system); subsequent runs are faster.

### Markdown (GFM) Documentation

Generate GitHub-Flavored Markdown documentation:

```bash
mvn dokka:dokka@dokkaMarkdown
```

This produces Markdown files in `build/dokka/markdown/` suitable for inclusion in GitHub repositories or READMEs.

### Generate Both Formats

Run both commands sequentially:

```bash
mvn dokka:dokka && mvn dokka:dokka@dokkaMarkdown
```

## Viewing Generated Documentation

### Viewing HTML Documentation

After generating HTML documentation:

1. **Via File Browser**:
   - Navigate to `build/dokka/html/` in your file explorer
   - Open `index.html` in your web browser

2. **Via Terminal** (Windows PowerShell):
   ```powershell
   cd build/dokka/html
   Start-Process index.html
   ```

3. **Via Terminal** (Linux/macOS):
   ```bash
   open build/dokka/html/index.html
   ```

The documentation includes:
- **Module Overview**: High-level project description
- **Packages**: All documented packages and their contents
- **Classes/Objects**: Detailed class documentation with KDoc comments
- **Functions**: All public functions with parameter and return descriptions
- **Properties**: Class properties with type information

### Viewing Markdown Documentation

The Markdown files are generated in `build/dokka/markdown/`:
- Can be viewed directly in any text editor
- Suitable for git repositories and GitHub READMEs
- Can be included in documentation wikis

## Project Documentation Structure

The Grade Calculator project includes comprehensive KDoc documentation for:

### **Model Layer** (`model/Student.kt`)
- `Student`: Data class representing a student record
  - Properties: name, studentId, caScore, examScore, totalScore, grade
  - Methods: computeTotal()

### **Domain Layer** (`domain/GradeCalculator.kt`)
- `GradeCalculator`: Singleton for grade assignment logic
  - Grading scale: A (70-100), B (60-69), C (50-59), D (45-49), E (40-44), F (<40)
  - Methods: assignGrade(student)
  - Features: Score validation, extensible design notes

### **Data Layer** (`data/FileHandler.kt`)
- `FileHandler`: File parsing and writing utilities
  - Supported formats: CSV, XLSX, XLS
  - Methods:
    - parse(file): Main entry point for file parsing
    - parseCsv(file): CSV-specific parsing
    - parseExcel(file): Excel-specific parsing
    - parseRowMap(map): Row-to-Student conversion
    - writeCsv(students, output): Export to CSV
  - Features: Flexible header matching, error handling

### **Utilities** (`utils/Statistics.kt`)
- `Statistics`: Class performance analysis
  - Methods:
    - average(students): Class average
    - highest(students): Maximum score
    - lowest(students): Minimum score
    - gradeDistribution(students): Grade frequency map

### **UI/Presentation** (`Main.kt`)
- `App()`: Main composable UI function
- `main()`: Application entry point

## Customizing Documentation

### Modifying Dokka Configuration

Edit `build.gradle.kts` to customize Dokka output:

```kotlin
tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets {
        configureEach {
            // Add more source files to documentation
            includes.from("README.md", "CONTRIBUTING.md")
            
            // Add GitHub source links
            sourceLink {
                localDirectory.set(file("src"))
                remoteUrl.set(java.net.URL("https://github.com/Tomikevine/Grade-Calculator-/tree/main/src"))
                remoteLineSuffix.set("#L")
            }
        }
    }
    // Change output directory
    outputDirectory.set(file("$buildDir/dokka/html"))
}
```

### Adding More KDoc Documentation

KDoc comments follow Javadoc-style syntax:

```kotlin
/**
 * Brief description of the function.
 *
 * Longer description with more details about what the function does,
 * its behavior, and any important notes.
 *
 * @param paramName Description of the parameter
 * @return Description of the return value
 * @see RelatedClass
 * @throws ExceptionType When this exception might be thrown
 */
fun myFunction(paramName: String): String {
    // implementation
}
```

Common tags:
- `@param`: Parameter documentation
- `@return`: Return value documentation
- `@see`: Cross-references to related classes/functions
- `@throws` / `@exception`: Exception documentation
- `@deprecated`: Mark as deprecated with migration info
- `@since`: API availability version

## Build Integration

Documentation generation is integrated into the Maven build lifecycle but does not execute automatically. To make it automatic:

1. Add to `pom.xml`:
```xml
<plugin>
    <groupId>org.jetbrains.dokka</groupId>
    <artifactId>dokka-maven-plugin</artifactId>
    <version>1.8.20</version>
    <executions>
        <execution>
            <phase>pre-site</phase>
            <goals>
                <goal>dokka</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

2. Then run:
```bash
mvn clean site
```

## Troubleshooting

### Issue: "Dokka task not found"
**Solution**: Ensure Dokka plugin is declared in `build.gradle.kts`:
```kotlin
plugins {
    id("org.jetbrains.dokka") version "1.8.20"
}
```

### Issue: "Module not documented"
**Solution**: Check that KDoc comments use proper `/** */` syntax, not `/* */`.

### Issue: "Links broken in HTML output"
**Solution**: Verify `@see` references use correct class/function names and are in scope.

### Issue: "build/dokka directory doesn't exist"
**Solution**: Run `mvn clean dokka:dokka` to ensure directory creation.

## Next Steps

- Review generated documentation at `build/dokka/html/index.html`
- Share HTML output with team members via web server or static hosting
- Include Markdown output in project wiki or README
- Update KDoc comments as code evolves
- Consider adding code examples to complex functions with code blocks:

```kotlin
/**
 * Example usage:
 * ```kotlin
 * val student = Student("Alice", "12345", 30.0, 45.0)
 * GradeCalculator.assignGrade(student)
 * println(student.grade) // Output: A
 * ```
 */
```

## Resources

- [Dokka Official Documentation](https://kotlinlang.org/docs/dokka-introduction.html)
- [KDoc Syntax](https://kotlinlang.org/docs/kotlin-doc.html)
- [Maven Dokka Plugin](https://mvnrepository.com/artifact/org.jetbrains.dokka/dokka-maven-plugin)
