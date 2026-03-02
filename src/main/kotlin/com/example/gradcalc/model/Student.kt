package com.example.gradcalc.model

/**
 * Represents a student record from a mark sheet with their academic scores and computed grade.
 *
 * This data class encapsulates all student information necessary for grade calculation.
 * The [totalScore] and [grade] are computed values that may be updated after instantiation.
 *
 * @property name The full name of the student.
 * @property studentId The unique identifier for the student (matric number or ID).
 * @property caScore The continuous assessment score, typically out of a fixed scale (default 0.0).
 * @property examScore The exam score, typically out of a fixed scale (default 0.0).
 * @property totalScore The total combined score. If not provided, it is computed from [caScore] and [examScore].
 * @property grade The assigned letter grade (A-F) based on [totalScore]. Defaults to empty string until computed.
 *
 * @see GradeCalculator
 */
data class Student(
    val name: String,
    val studentId: String,
    val caScore: Double = 0.0,
    val examScore: Double = 0.0,
    var totalScore: Double = 0.0,
    var grade: String = ""
) {
    /**
     * Computes the total score if it was not explicitly provided.
     *
     * If [totalScore] is 0 or less, it is calculated as the sum of [caScore] and [examScore].
     * If [totalScore] is already set to a positive value, it is left unchanged.
     */
    fun computeTotal() {
        totalScore = if (totalScore <= 0.0) caScore + examScore else totalScore
    }
}