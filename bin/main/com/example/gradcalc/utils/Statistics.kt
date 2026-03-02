package com.example.gradcalc.utils

import com.example.gradcalc.model.Student

/**
 * Provides statistical analysis functions for a class of student records.
 *
 * This singleton object computes summary statistics such as class average,
 * highest and lowest scores, and grade distribution. These metrics are useful
 * for generating reports and understanding overall class performance.
 *
 * @see Student
 */
object Statistics {
    /**
     * Calculates the arithmetic mean (average) of all student total scores.
     *
     * @param students The list of student records to analyze.
     * @return The average total score across all students, or 0.0 if the list is empty.
     */
    fun average(students: List<Student>): Double {
        return if (students.isEmpty()) 0.0 else students.map { it.totalScore }.average()
    }

    /**
     * Finds the highest total score among all students.
     *
     * @param students The list of student records to analyze.
     * @return The maximum total score, or 0.0 if the list is empty.
     */
    fun highest(students: List<Student>): Double {
        return students.maxOfOrNull { it.totalScore } ?: 0.0
    }

    /**
     * Finds the lowest total score among all students.
     *
     * @param students The list of student records to analyze.
     * @return The minimum total score, or 0.0 if the list is empty.
     */
    fun lowest(students: List<Student>): Double {
        return students.minOfOrNull { it.totalScore } ?: 0.0
    }

    /**
     * Calculates the distribution of grades across all students.
     *
     * Counts how many students received each letter grade (A, B, C, D, E, F, Invalid).
     *
     * @param students The list of student records to analyze.
     * @return A map where keys are letter grades and values are the count of students with that grade.
     */
    fun gradeDistribution(students: List<Student>): Map<String, Long> {
        return students.groupingBy { it.grade }.eachCount().mapValues { it.value.toLong() }
    }
}
