package com.example.gradcalc.domain

import com.example.gradcalc.model.Student
import kotlin.ExperimentalStdlibApi

@OptIn(ExperimentalStdlibApi::class)
/**
 * Singleton object responsible for assigning letter grades to students based on their total scores.
 *
 * ## Grading Scale
 * The default grading scale is as follows:
 * - **A**: 70-100 points
 * - **B**: 60-69 points
 * - **C**: 50-59 points
 * - **D**: 45-49 points
 * - **E**: 40-44 points
 * - **F**: 0-39 points
 *
 * ## Design Notes
 * The grading scale is currently hardcoded but could be made configurable in future versions
 * to allow lecturers to adjust cutoffs dynamically. Valid scores must fall within the 0-100 range;
 * scores outside this range will result in an "Invalid" grade.
 *
 * @see Student
 */
object GradeCalculator {
    // default scale; could be loaded from config in future
    private val scale = listOf(
        70.0 to 100.0 to "A",
        60.0 to 69.0 to "B",
        50.0 to 59.0 to "C",
        45.0 to 49.0 to "D",
        40.0 to 44.0 to "E",
        0.0 to 39.99 to "F"
    )

    /**
     * Assigns a letter grade to the provided student based on their total score.
     *
     * This function performs the following steps:
     * 1. Computes the student's total score if not explicitly set
     * 2. Validates that the total score is within the range [0.0, 100.0]
     * 3. Assigns the appropriate letter grade based on the configured grading scale
     *
     * Invalid scores (below 0 or above 100) will receive an "Invalid" grade marker.
     *
     * @param student The student record to assign a grade to. The student's [Student.grade] property is modified in place.
     *
     * @see Student.computeTotal
     */
    fun assignGrade(student: Student) {
        student.computeTotal()
        // validate scores are within 0..100
        if (student.totalScore < 0 || student.totalScore > 100) {
            student.grade = "Invalid"
            return
        }
        student.grade = when (student.totalScore) {
            in 70.0..100.0 -> "A"
            in 60.0..<70.0 -> "B"
            in 50.0..<60.0 -> "C"
            in 45.0..<50.0 -> "D"
            in 40.0..<45.0 -> "E"
            in 0.0..<40.0 -> "F"
            else -> "Invalid"
        }
    }
}
