package com.gradecalculator.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class GradeCalculatorTest {

    private val calculator = GradeCalculator()

    @Test
    fun `returns Invalid when mark is null`() {
        assertEquals("Invalid", calculator.calculateGrade(null))
    }

    @Test
    fun `returns Invalid when mark is negative`() {
        assertEquals("Invalid", calculator.calculateGrade(-0.01))
    }

    @Test
    fun `assigns correct grades on boundaries`() {
        val cases = listOf(
            100.0 to "A",
            70.0 to "A",
            69.99 to "B",
            60.0 to "B",
            59.99 to "C",
            50.0 to "C",
            49.99 to "D",
            45.0 to "D",
            44.99 to "E",
            40.0 to "E",
            39.99 to "F",
            0.0 to "F"
        )

        cases.forEach { (mark, expected) ->
            assertEquals(expected, calculator.calculateGrade(mark), "mark=$mark")
        }
    }
}
