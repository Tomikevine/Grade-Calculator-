package com.gradecalculator.data.parser

import com.gradecalculator.data.model.Student
import com.opencsv.CSVReader
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.nio.file.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ExcelParserTest {

    @TempDir
    lateinit var tempDir: Path

    private val parser = ExcelParser()

    @Test
    fun `readFile parses csv aliases and computes fallback total`() {
        val input = tempDir.resolve("input.csv").toFile()
        input.writeText(
            """
            Full Name,Reg No,CA,Exam,Overall
            Ada Lovelace,ST-001,30,40,
            Grace Hopper,ST-002,10,20,99
            ,,,,
            """.trimIndent()
        )

        val students = parser.readFile(input.path)

        assertEquals(2, students.size)
        assertEquals("Ada Lovelace", students[0].name)
        assertEquals("ST-001", students[0].studentId)
        assertEquals(70.0, students[0].totalScore)
        assertEquals(99.0, students[1].totalScore)
    }

    @Test
    fun `readFile parses xlsx file`() {
        val input = tempDir.resolve("input.xlsx").toFile()
        createExcelInput(file = input, extension = "xlsx", includeTotal = false)

        val students = parser.readFile(input.path)

        assertEquals(1, students.size)
        assertEquals("Excel Student", students[0].name)
        assertEquals("EX-001", students[0].studentId)
        assertEquals(80.0, students[0].totalScore)
    }

    @Test
    fun `readFile parses xls file and uses provided total`() {
        val input = tempDir.resolve("input.xls").toFile()
        createExcelInput(file = input, extension = "xls", includeTotal = true)

        val students = parser.readFile(input.path)

        assertEquals(1, students.size)
        assertEquals(95.0, students[0].totalScore)
    }

    @Test
    fun `writeFile writes csv output`() {
        val output = tempDir.resolve("output.csv").toFile()

        parser.writeFile(output.path, sampleStudents())

        assertTrue(output.exists())
        CSVReader(FileReader(output)).use { csvReader ->
            val rows = csvReader.readAll()
            assertEquals(3, rows.size)
            assertEquals(listOf("Name", "Student ID", "CA Score", "Exam Score", "Total Score", "Grade"), rows[0].toList())
            assertEquals("Ada", rows[1][0])
            assertEquals("A", rows[1][5])
            assertEquals("Bob", rows[2][0])
            assertEquals("C", rows[2][5])
        }
    }

    @Test
    fun `writeFile writes xlsx output`() {
        assertWorkbookOutput("xlsx")
    }

    @Test
    fun `writeFile writes xls output`() {
        assertWorkbookOutput("xls")
    }

    @Test
    fun `readFile rejects unsupported extension`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            parser.readFile("students.txt")
        }
        assertTrue(exception.message.orEmpty().contains("Unsupported file type"))
    }

    @Test
    fun `writeFile rejects unsupported extension`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            parser.writeFile("output.pdf", sampleStudents())
        }
        assertTrue(exception.message.orEmpty().contains("Unsupported output type"))
    }

    @Test
    fun `readFile fails when required headers are missing`() {
        val input = tempDir.resolve("bad.csv").toFile()
        input.writeText(
            """
            First,Second,Third
            A,B,C
            """.trimIndent()
        )

        val exception = assertFailsWith<IllegalArgumentException> {
            parser.readFile(input.path)
        }
        assertTrue(exception.message.orEmpty().contains("Missing required column"))
    }

    private fun assertWorkbookOutput(extension: String) {
        val output = tempDir.resolve("output.$extension").toFile()

        parser.writeFile(output.path, sampleStudents())

        assertTrue(output.exists())
        WorkbookFactory.create(output).use { workbook ->
            val sheet = workbook.getSheetAt(0)
            assertEquals("Name", sheet.getRow(0).getCell(0).stringCellValue)
            assertEquals("Student ID", sheet.getRow(0).getCell(1).stringCellValue)
            assertEquals("Ada", sheet.getRow(1).getCell(0).stringCellValue)
            assertEquals("ST-001", sheet.getRow(1).getCell(1).stringCellValue)
            assertEquals(82.0, sheet.getRow(1).getCell(4).numericCellValue, 0.0001)
            assertEquals("A", sheet.getRow(1).getCell(5).stringCellValue)
        }
    }

    private fun createExcelInput(file: File, extension: String, includeTotal: Boolean) {
        val workbook: Workbook = if (extension == "xls") HSSFWorkbook() else XSSFWorkbook()
        val sheet = workbook.createSheet("Sheet1")

        val header = sheet.createRow(0)
        header.createCell(0).setCellValue("Name")
        header.createCell(1).setCellValue("Student ID")
        header.createCell(2).setCellValue("CA Score")
        header.createCell(3).setCellValue("Exam Score")
        if (includeTotal) {
            header.createCell(4).setCellValue("Total Score")
        }

        val row = sheet.createRow(1)
        row.createCell(0).setCellValue("Excel Student")
        row.createCell(1).setCellValue("EX-001")
        row.createCell(2).setCellValue(35.0)
        row.createCell(3).setCellValue(45.0)
        if (includeTotal) {
            row.createCell(4).setCellValue(95.0)
        }

        FileOutputStream(file).use { workbook.write(it) }
        workbook.close()
    }

    private fun sampleStudents(): List<Student> {
        return listOf(
            Student("Ada", "ST-001", 37.0, 45.0, 82.0, "A"),
            Student("Bob", "ST-002", 24.0, 30.0, 54.0, "C")
        )
    }
}
