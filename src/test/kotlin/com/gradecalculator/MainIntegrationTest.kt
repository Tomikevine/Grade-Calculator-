package com.gradecalculator

import com.opencsv.CSVReader
import org.junit.jupiter.api.io.TempDir
import java.io.ByteArrayOutputStream
import java.io.FileReader
import java.io.PrintStream
import java.nio.file.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MainIntegrationTest {

    @TempDir
    lateinit var tempDir: Path

    @Test
    fun `main processes file and writes graded output when args are provided`() {
        val input = tempDir.resolve("input.csv").toFile()
        val output = tempDir.resolve("graded.csv").toFile()
        input.writeText(
            """
            Name,Student ID,CA Score,Exam Score
            Ada,ST-001,40,35
            Bob,ST-002,25,30
            """.trimIndent()
        )

        val console = captureStdOut {
            main(arrayOf(input.path, output.path))
        }

        assertTrue(output.exists())
        assertTrue(console.contains("Grading complete. Output saved as"))
        CSVReader(FileReader(output)).use { reader ->
            val rows = reader.readAll()
            assertEquals(3, rows.size)
            assertEquals("Ada", rows[1][0])
            assertEquals("A", rows[1][5])
            assertEquals("Bob", rows[2][0])
            assertEquals("C", rows[2][5])
        }
    }

    @Test
    fun `main prints error when input file does not exist`() {
        val missingInput = tempDir.resolve("missing.csv").toFile()
        val output = tempDir.resolve("graded.csv").toFile()

        val console = captureStdOut {
            main(arrayOf(missingInput.path, output.path))
        }

        assertTrue(console.contains("Input file not found"))
        assertFalse(output.exists())
    }

    @Test
    fun `main reports empty student data and does not write output`() {
        val input = tempDir.resolve("empty.csv").toFile()
        val output = tempDir.resolve("graded.csv").toFile()
        input.writeText("Name,Student ID,CA Score,Exam Score")

        val console = captureStdOut {
            main(arrayOf(input.path, output.path))
        }

        assertTrue(console.contains("No student rows were found in the uploaded file."))
        assertFalse(output.exists())
    }

    @Test
    fun `main accepts quoted paths in args`() {
        val input = tempDir.resolve("quoted-input.csv").toFile()
        val output = tempDir.resolve("quoted-output.csv").toFile()
        input.writeText(
            """
            Name,Student ID,CA Score,Exam Score
            Ada,ST-001,40,35
            """.trimIndent()
        )

        captureStdOut {
            main(arrayOf("\"${input.path}\"", "\"${output.path}\""))
        }

        assertTrue(output.exists())
    }

    private fun captureStdOut(block: () -> Unit): String {
        val originalOut = System.out
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream, true, "UTF-8"))
        try {
            block()
        } finally {
            System.setOut(originalOut)
        }
        return outputStream.toString("UTF-8")
    }
}
