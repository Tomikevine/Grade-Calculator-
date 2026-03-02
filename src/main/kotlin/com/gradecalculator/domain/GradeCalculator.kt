package com.gradecalculator.domain

/**
 * Converts a numeric total score into a letter grade using the default scale:
 * `A(70+)`, `B(60-69)`, `C(50-59)`, `D(45-49)`, `E(40-44)`, `F(0-39)`.
 */
class GradeCalculator {

    /**
     * Returns the letter grade for [mark].
     *
     * @param mark The student's total score. `null` returns `"Invalid"`.
     * @return One of `"A"`, `"B"`, `"C"`, `"D"`, `"E"`, `"F"`, or `"Invalid"`.
     */
    fun calculateGrade(mark: Double?): String {

        val validMark = mark ?: return "Invalid"

        return when {
            validMark >= 70 -> "A"
            validMark >= 60 -> "B"
            validMark >= 50 -> "C"
            validMark >= 45 -> "D"
            validMark >= 40 -> "E"
            validMark >= 0  -> "F"
            else -> "Invalid"
        }
    }
}
