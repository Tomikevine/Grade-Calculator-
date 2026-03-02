package com.gradecalculator.data.parser

import com.gradecalculator.data.model.Student
import com.opencsv.CSVReader
import com.opencsv.CSVWriter
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileReader
import java.io.FileWriter
import java.util.Locale

/**
 * File parser/exporter for mark sheets.
 *
 * Supports reading `.csv`, `.xlsx`, and `.xls` files and writing graded output
 * to `.csv`, `.xlsx`, or `.xls`.
 */
class ExcelParser {

    /**
     * Reads student records from a supported file.
     *
     * @param filePath Path to the input file.
     * @return Parsed student rows.
     * @throws IllegalArgumentException If the file extension is unsupported.
     */
    fun readFile(filePath: String): List<Student> {
        val extension = File(filePath).extension.lowercase(Locale.ROOT)
        return when (extension) {
            "csv" -> readCsv(filePath)
            "xlsx", "xls" -> readExcel(filePath)
            else -> throw IllegalArgumentException("Unsupported file type: .$extension. Use .csv, .xlsx, or .xls.")
        }
    }

    /**
     * Writes graded students to a supported output file format.
     *
     * @param filePath Output file path.
     * @param students Students to export.
     * @throws IllegalArgumentException If the output extension is unsupported.
     */
    fun writeFile(filePath: String, students: List<Student>) {
        val extension = File(filePath).extension.lowercase(Locale.ROOT)
        when (extension) {
            "csv" -> writeCsv(filePath, students)
            "xlsx", "xls" -> writeExcel(filePath, students, extension)
            else -> throw IllegalArgumentException("Unsupported output type: .$extension. Use .csv, .xlsx, or .xls.")
        }
    }

    private fun readExcel(filePath: String): List<Student> {

        val students = mutableListOf<Student>()
        val formatter = DataFormatter()

        FileInputStream(File(filePath)).use { fis ->
            val workbook = WorkbookFactory.create(fis)
            val sheet = workbook.getSheetAt(0) ?: return students

            val header = sheet.getRow(0) ?: return students
            val headerColumns = (0 until header.lastCellNum).map { columnIndex ->
                formatter.formatCellValue(header.getCell(columnIndex)).trim()
            }
            val headerIndex = buildHeaderIndex(headerColumns)

            for (row in sheet.drop(1)) {
                parseStudentRow(row, headerIndex, formatter)?.let { students.add(it) }
            }

            workbook.close()
        }

        return students
    }

    private fun readCsv(filePath: String): List<Student> {
        val students = mutableListOf<Student>()

        CSVReader(FileReader(filePath)).use { csvReader ->
            val allRows = csvReader.readAll()
            if (allRows.isEmpty()) {
                return students
            }

            val headerIndex = buildHeaderIndex(allRows.first().map { it.trim() })

            allRows.drop(1).forEach { columns ->
                parseStudentRow(columns.toList(), headerIndex)?.let { students.add(it) }
            }
        }

        return students
    }

    private fun writeExcel(filePath: String, students: List<Student>, extension: String) {

        val workbook: Workbook = when (extension) {
            "xls" -> HSSFWorkbook()
            else -> XSSFWorkbook()
        }
        val sheet = workbook.createSheet("Graded Students")

        val header = sheet.createRow(0)
        header.createCell(0).setCellValue("Name")
        header.createCell(1).setCellValue("Student ID")
        header.createCell(2).setCellValue("CA Score")
        header.createCell(3).setCellValue("Exam Score")
        header.createCell(4).setCellValue("Total Score")
        header.createCell(5).setCellValue("Grade")

        students.forEachIndexed { index, student ->
            val row = sheet.createRow(index + 1)

            row.createCell(0).setCellValue(student.name)
            row.createCell(1).setCellValue(student.studentId)
            row.createCell(2).setCellValue(student.caScore)
            row.createCell(3).setCellValue(student.examScore)
            row.createCell(4).setCellValue(student.totalScore)
            row.createCell(5).setCellValue(student.grade ?: "Invalid")
        }

        FileOutputStream(filePath).use { fos ->
            workbook.write(fos)
        }

        workbook.close()
    }

    private fun writeCsv(filePath: String, students: List<Student>) {
        CSVWriter(FileWriter(filePath)).use { csvWriter ->
            csvWriter.writeNext(arrayOf("Name", "Student ID", "CA Score", "Exam Score", "Total Score", "Grade"))
            students.forEach { student ->
                csvWriter.writeNext(
                    arrayOf(
                        student.name,
                        student.studentId,
                        formatScore(student.caScore),
                        formatScore(student.examScore),
                        formatScore(student.totalScore),
                        student.grade ?: "Invalid"
                    )
                )
            }
        }
    }

    private fun parseStudentRow(
        row: Row,
        headerIndex: HeaderIndex,
        formatter: DataFormatter
    ): Student? {
        val name = formatter.formatCellValue(row.getCell(headerIndex.nameIndex)).trim()
        val studentId = formatter.formatCellValue(row.getCell(headerIndex.studentIdIndex)).trim()
        if (name.isBlank() && studentId.isBlank()) {
            return null
        }

        val caScore = getNumericValue(getCell(row, headerIndex.caScoreIndex), formatter)
        val examScore = getNumericValue(getCell(row, headerIndex.examScoreIndex), formatter)
        val totalScore = headerIndex.totalScoreIndex?.let {
            getOptionalNumericValue(row.getCell(it), formatter)
        } ?: (caScore + examScore)

        return Student(
            name = name.ifBlank { "Unknown" },
            studentId = studentId.ifBlank { "Unknown" },
            caScore = caScore,
            examScore = examScore,
            totalScore = totalScore
        )
    }

    private fun parseStudentRow(
        columns: List<String>,
        headerIndex: HeaderIndex
    ): Student? {
        val name = getColumn(columns, headerIndex.nameIndex)
        val studentId = getColumn(columns, headerIndex.studentIdIndex)
        if (name.isBlank() && studentId.isBlank()) {
            return null
        }

        val caScore = getColumn(columns, headerIndex.caScoreIndex).toDoubleOrNull() ?: 0.0
        val examScore = getColumn(columns, headerIndex.examScoreIndex).toDoubleOrNull() ?: 0.0
        val totalScore = headerIndex.totalScoreIndex?.let {
            val raw = getColumn(columns, it)
            raw.takeIf { value -> value.isNotBlank() }?.toDoubleOrNull()
        } ?: (caScore + examScore)

        return Student(
            name = name.ifBlank { "Unknown" },
            studentId = studentId.ifBlank { "Unknown" },
            caScore = caScore,
            examScore = examScore,
            totalScore = totalScore
        )
    }

    private fun buildHeaderIndex(header: List<String>): HeaderIndex {
        val normalized = header.map { it.lowercase(Locale.ROOT) }

        val nameIndex = findHeaderIndex(normalized, NAME_HEADERS)
            ?: throw IllegalArgumentException("Missing required column: Name")
        val studentIdIndex = findHeaderIndex(normalized, STUDENT_ID_HEADERS)
            ?: throw IllegalArgumentException("Missing required column: Student ID")

        return HeaderIndex(
            nameIndex = nameIndex,
            studentIdIndex = studentIdIndex,
            caScoreIndex = findHeaderIndex(normalized, CA_SCORE_HEADERS),
            examScoreIndex = findHeaderIndex(normalized, EXAM_SCORE_HEADERS),
            totalScoreIndex = findHeaderIndex(normalized, TOTAL_SCORE_HEADERS)
        )
    }

    private fun findHeaderIndex(header: List<String>, aliases: Set<String>): Int? {
        val index = header.indexOfFirst { aliases.contains(it) }
        return if (index >= 0) index else null
    }

    private fun getNumericValue(cell: Cell?, formatter: DataFormatter): Double {
        if (cell == null) {
            return 0.0
        }

        return when (cell.cellType) {
            CellType.NUMERIC -> cell.numericCellValue
            CellType.FORMULA -> runCatching { cell.numericCellValue }.getOrElse {
                formatter.formatCellValue(cell).toDoubleOrNull() ?: 0.0
            }
            else -> formatter.formatCellValue(cell).toDoubleOrNull() ?: 0.0
        }
    }

    private fun getOptionalNumericValue(cell: Cell?, formatter: DataFormatter): Double? {
        if (cell == null) {
            return null
        }

        return when (cell.cellType) {
            CellType.NUMERIC -> cell.numericCellValue
            CellType.FORMULA -> runCatching { cell.numericCellValue }.getOrElse {
                formatter.formatCellValue(cell).trim().toDoubleOrNull()
            }
            else -> formatter.formatCellValue(cell).trim().toDoubleOrNull()
        }
    }

    private fun getCell(row: Row, index: Int?): Cell? {
        if (index == null || index < 0) {
            return null
        }
        return row.getCell(index)
    }

    private fun getColumn(columns: List<String>, index: Int?): String {
        if (index == null || index < 0 || index >= columns.size) {
            return ""
        }
        return columns[index].trim()
    }

    private fun formatScore(value: Double): String {
        return if (value % 1.0 == 0.0) {
            value.toInt().toString()
        } else {
            String.format(Locale.ROOT, "%.2f", value).trimEnd('0').trimEnd('.')
        }
    }

    private data class HeaderIndex(
        val nameIndex: Int,
        val studentIdIndex: Int,
        val caScoreIndex: Int?,
        val examScoreIndex: Int?,
        val totalScoreIndex: Int?
    )

    companion object {
        private val NAME_HEADERS = setOf("name", "student name", "full name", "student")
        private val STUDENT_ID_HEADERS = setOf("student id", "id", "matricule", "matric", "matric no", "reg no")
        private val CA_SCORE_HEADERS = setOf("ca score", "ca", "continuous assessment", "ca/test")
        private val EXAM_SCORE_HEADERS = setOf("exam score", "exam", "examination", "exam mark")
        private val TOTAL_SCORE_HEADERS = setOf("total score", "total", "score", "mark", "overall")
    }
}
