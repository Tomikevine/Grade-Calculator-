package com.example.gradcalc.data\n\nimport com.example.gradcalc.model.Student\nimport com.github.doyaaaaaken.kotlincsv.dsl.csvReader\nimport com.github.doyaaaaaken.kotlincsv.dsl.csvWriter\nimport org.apache.poi.ss.usermodel.CellType\nimport org.apache.poi.ss.usermodel.WorkbookFactory\nimport java.io.File\nimport java.io.FileInputStream\nimport java.io.FileOutputStream\n\n/**\n * Handles reading and writing student mark sheet files in multiple formats.\n *\n * This singleton provides utilities for parsing student data from CSV and Excel files,\n * as well as writing processed student records (including computed grades) back to CSV format.\n *\n * ## Supported Formats\n * - **CSV**: Comma-separated values with flexible header mapping\n * - **Excel**: XLSX and XLS formats with cell-type-aware reading\n *\n * ## Header Flexibility\n * The parser accepts various common header names for maximum compatibility:\n * - Student names: \"Student Name\", \"Name\"\n * - Student IDs: \"Matric Number\", \"ID\", \"studentId\"\n * - CA scores: \"CA\", \"CA Score\"\n * - Exam scores: \"Exam\", \"Exam Score\"\n * - Total scores: \"Total\"\n *\n * Malformed rows or missing critical fields are silently skipped rather than failing the entire parse.\n *\n * @see Student\n */\nobject FileHandler {\n    /**\n     * Parses a mark sheet file and returns a list of student records.\n     *\n     * The file format is determined by its extension (.csv, .xlsx, or .xls).\n     * Each row in the file is mapped to a [Student] object.\n     *\n     * @param file The mark sheet file to parse.\n     * @return A list of [Student] objects parsed from the file. Rows with invalid or missing critical data are excluded.\n     * @throws IllegalArgumentException if the file format is not supported.\n     *\n     * @see parseCsv\n     * @see parseExcel\n     */\n    fun parse(file: File): List<Student> {\n        return when (file.extension.lowercase()) {\n            \"csv\" -> parseCsv(file)\n            \"xlsx\", \"xls\" -> parseExcel(file)\n            else -> throw IllegalArgumentException(\"Unsupported file type: \").plus(file.extension)\n        }\n    }

    /**
     * Parses a CSV file and extracts student records.
     *
     * The CSV file must have a header row with column names. The parser is flexible
     * and recognizes various common header variations. Non-critical fields may be omitted;
     * rows with missing name or student ID are skipped.
     *
     * @param file The CSV file to parse.
     * @return A list of [Student] objects, excluding rows with parsing errors.
     *
     * @see parse
     */
    private fun parseCsv(file: File): List<Student> {
        val rows = csvReader().readAllWithHeader(file)
        return rows.mapNotNull { map ->
            parseRowMap(map)
        }
    }

    /**
     * Parses an Excel file (.xls or .xlsx) and extracts student records.
     *
     * Assumes the first sheet contains the data and the first row contains headers.
     * Cell values are converted to strings based on their cell type (STRING, NUMERIC, etc.).
     * Rows with parsing errors are silently skipped.
     *
     * @param file The Excel file to parse.
     * @return A list of [Student] objects, excluding rows with parsing errors or missing required fields.
     *
     * @see parse
     */
    private fun parseExcel(file: File): List<Student> {
        FileInputStream(file).use { fis ->
            val workbook = WorkbookFactory.create(fis)
            val sheet = workbook.getSheetAt(0)
            val header = sheet.getRow(0) ?: return emptyList()
            val headers = header.map { it.stringCellValue.trim() }
            val rows = mutableListOf<Student>()
            for (r in 1..sheet.lastRowNum) {
                val row = sheet.getRow(r) ?: continue
                val values = headers.mapIndexed { idx, _ ->
                    val cell = row.getCell(idx)
                    when (cell?.cellType) {
                        CellType.STRING -> cell.stringCellValue
                        CellType.NUMERIC -> cell.numericCellValue.toString()
                        else -> ""
                    }
                }
                val map = headers.zip(values).toMap()
                val student = parseRowMap(map)
                if (student != null) rows.add(student)
            }
            return rows
        }
    }

    /**
     * Parses a single row (as a map of header to value) into a [Student] object.
     *
     * Performs flexible header matching and type conversion. Returns null if critical
     * fields (name or student ID) are missing or if an exception occurs during parsing.
     *
     * @param map A map of column headers to cell values.
     * @return A [Student] object if parsing succeeds, or null if required fields are missing.
     *
     * @see parseCsv
     * @see parseExcel
     */
    private fun parseRowMap(map: Map<String, String>): Student? {
        return try {
            val name = map["Student Name"] ?: map["Name"] ?: return null
            val id = map["Matric Number"] ?: map["ID"] ?: map["studentId"] ?: return null
            val ca = map["CA"]?.toDoubleOrNull() ?: map["CA Score"]?.toDoubleOrNull() ?: 0.0
            val exam = map["Exam"]?.toDoubleOrNull() ?: map["Exam Score"]?.toDoubleOrNull() ?: 0.0
            val total = map["Total"]?.toDoubleOrNull() ?: 0.0
            Student(name, id, ca, exam, total)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Writes a list of student records to a CSV file.
     *
     * The output CSV contains the following columns in order:
     * Student Name, Matric Number, CA, Exam, Total, Grade
     *
     * Each student record is written as a single row with comma separation.
     * This is typically used to export processed student data after grade calculation.
     *
     * @param students The list of [Student] objects to write.
     * @param output The file to write to. If it exists, it will be overwritten.
     *
     * @see parse
     */
    fun writeCsv(students: List<Student>, output: File) {
        csvWriter().open(output) {
            writeRow(listOf("Student Name", "Matric Number", "CA", "Exam", "Total", "Grade"))
            students.forEach { s ->
                writeRow(listOf(s.name, s.studentId, s.caScore.toString(), s.examScore.toString(), s.totalScore.toString(), s.grade))
            }
        }
    }
}
