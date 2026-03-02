package com.example.gradcalc.console

import com.example.gradcalc.data.FileHandler
import com.example.gradcalc.domain.GradeCalculator
import com.example.gradcalc.utils.Statistics
import java.io.File
import java.util.*

/**
 * Console-based entry point for the Grade Calculator.
 *
 * Provides an interactive command-line interface for processing mark sheets
 * without requiring desktop dependencies.
 */
fun main() {
    val scanner = Scanner(System.`in`)
    
    println("╔════════════════════════════════════════╗")
    println("║      Grade Calculator - Console        ║")
    println("╚════════════════════════════════════════╝")
    println()
    
    while (true) {
        println("Menu:")
        println("1. Process mark sheet")
        println("2. View sample data")
        println("3. View grading scale")
        println("4. Exit")
        print("Select option (1-4): ")
        
        when (scanner.nextLine().trim()) {
            "1" -> processMarkSheet(scanner)
            "2" -> viewSampleData()
            "3" -> viewGradingScale()
            "4" -> {
                println("Goodbye!")
                break
            }
            else -> println("Invalid option. Please try again.\n")
        }
    }
}

private fun processMarkSheet(scanner: Scanner) {
    println()
    print("Enter file path (e.g., sample_input.csv): ")
    val filePath = scanner.nextLine().trim()
    val file = File(filePath)
    
    if (!file.exists()) {
        println("❌ File not found: $filePath\n")
        return
    }
    
    try {
        println("\n📂 Parsing file...")
        val students = FileHandler.parse(file)
        
        if (students.isEmpty()) {
            println("❌ No valid student records found\n")
            return
        }
        
        println("✓ Found ${students.size} students\n")
        
        // Calculate grades
        println("📊 Calculating grades...")
        students.forEach { GradeCalculator.assignGrade(it) }
        println("✓ Grades assigned\n")
        
        // Save results
        val outputFile = File(file.parentFile, "graded_" + file.nameWithoutExtension + ".csv")
        FileHandler.writeCsv(students, outputFile)
        
        println("📝 Results saved to: ${outputFile.absolutePath}")
        println()
        
        // Display statistics
        displayStatistics(students)
        
        // Show sample results
        println("\n📋 Sample Student Records:")
        println("─".repeat(70))
        students.take(3).forEach { student ->
            println("Name: ${student.name} | ID: ${student.studentId} | Score: ${student.totalScore} | Grade: ${student.grade}")
        }
        if (students.size > 3) println("... and ${students.size - 3} more students")
        println()
        
    } catch (e: Exception) {
        println("❌ Error processing file: ${e.message}\n")
    }
}

private fun displayStatistics(students: List<com.example.gradcalc.model.Student>) {
    println("📈 Class Statistics:")
    println("─".repeat(40))
    println("Average Score: %.2f".format(Statistics.average(students)))
    println("Highest Score: %.2f".format(Statistics.highest(students)))
    println("Lowest Score: %.2f".format(Statistics.lowest(students)))
    println()
    
    val distribution = Statistics.gradeDistribution(students)
    println("Grade Distribution:")
    distribution.toSortedMap().forEach { (grade, count) ->
        val bar = "█".repeat(count.toInt())
        println("  $grade: $bar ($count students)")
    }
}

private fun viewSampleData() {
    println()
    println("📋 Sample Input Format:")
    println("─".repeat(50))
    println("Student Name,Matric Number,CA,Exam")
    println("Alice,12345,30,45")
    println("Bob,23456,25,30")
    println("Charlie,34567,40,35")
    println()
    println("💡 Try this with: sample_input.csv")
    println()
}

private fun viewGradingScale() {
    println()
    println("📊 Grading Scale:")
    println("─".repeat(30))
    println("A: 70 - 100  (Excellent)")
    println("B: 60 - 69   (Good)")
    println("C: 50 - 59   (Satisfactory)")
    println("D: 45 - 49   (Acceptable)")
    println("E: 40 - 44   (Pass)")
    println("F: 0  - 39   (Fail)")
    println()
}
