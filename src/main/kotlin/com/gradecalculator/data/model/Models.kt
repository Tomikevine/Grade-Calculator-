package com.gradecalculator.data.model

/**
 * Represents a single student row parsed from CSV/Excel input.
 *
 * @property name Student full name.
 * @property studentId Student identifier (matric/reg number).
 * @property caScore Continuous Assessment score.
 * @property examScore Examination score.
 * @property totalScore Final score used for grade computation.
 * @property grade Computed letter grade, or `null` before grading.
 */
data class Student(
    val name: String,
    val studentId: String,
    val caScore: Double,
    val examScore: Double,
    val totalScore: Double,
    var grade: String? = null
)
