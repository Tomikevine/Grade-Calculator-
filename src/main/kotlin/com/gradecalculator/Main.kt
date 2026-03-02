package com.gradecalculator

import com.gradecalculator.data.parser.ExcelParser
import com.gradecalculator.domain.GradeCalculator
import java.io.File
import java.util.Locale

/**
 * Application entry point.
 *
 * CLI usage:
 * - `run --args="<inputPath>"` to process with interactive output prompts.
 * - `run --args="<inputPath> <outputPath>"` to process fully non-interactively.
 */
fun main(args: Array<String>) {
    val inputPath = sanitizePath(args.firstOrNull()).ifBlank {
        promptForInputPath()
    }
    if (inputPath.isBlank()) {
        println("No input file path provided.")
        return
    }
    val inputFile = File(inputPath)
    if (!inputFile.exists() || !inputFile.isFile) {
        println("Input file not found: $inputPath")
        return
    }

    val parser = ExcelParser()
    val calculator = GradeCalculator()
    val outputPath = sanitizePath(args.getOrNull(1)).ifBlank {
        promptForOutputPath(inputPath)
    }
    if (outputPath.isBlank()) {
        println("No output path provided.")
        return
    }
    File(outputPath).parentFile?.mkdirs()

    try {
        val students = parser.readFile(inputPath)
        if (students.isEmpty()) {
            println("No student rows were found in the uploaded file.")
            return
        }

        val gradedStudents = students.map { student ->
            val grade = calculator.calculateGrade(student.totalScore)
            student.copy(grade = grade)
        }

        parser.writeFile(outputPath, gradedStudents)

        println("Grading complete. Output saved as $outputPath")
    } catch (exception: Exception) {
        println("Failed to process file: ${exception.message}")
    }
}

/**
 * Prompts the user to provide an input mark sheet path.
 */
private fun promptForInputPath(): String {
    println("Upload by entering file path (.csv, .xlsx, .xls):")
    return sanitizePath(readLine())
}

/**
 * Prompts for desired output format and path, then returns the resolved output path.
 */
private fun promptForOutputPath(inputPath: String): String {
    val inputExtension = File(inputPath).extension.lowercase(Locale.ROOT)
    println("Choose output format: [1] CSV  [2] Excel (.xlsx)  [3] Excel (.xls)  [Enter = same as input]")
    val choice = readLine()?.trim().orEmpty()
    val outputExtension = when (choice) {
        "1" -> "csv"
        "2" -> "xlsx"
        "3" -> "xls"
        else -> if (inputExtension in setOf("csv", "xlsx", "xls")) inputExtension else "xlsx"
    }
    val defaultPath = defaultOutputPath(inputPath, outputExtension)
    println("Enter output file path (or press Enter for default: $defaultPath):")
    return sanitizePath(readLine()).ifBlank { defaultPath }
}

/**
 * Builds a default graded output path beside the input file.
 */
private fun defaultOutputPath(inputPath: String, outputExtension: String): String {
    val inputFile = File(inputPath)
    val outputFileName = "${inputFile.nameWithoutExtension}_graded.$outputExtension"
    val parent = inputFile.parentFile ?: return outputFileName
    return File(parent, outputFileName).path
}

/**
 * Trims leading/trailing spaces and surrounding single/double quotes from user-provided paths.
 */
private fun sanitizePath(path: String?): String {
    return path
        ?.trim()
        ?.trim('"')
        ?.trim('\'')
        .orEmpty()
}
