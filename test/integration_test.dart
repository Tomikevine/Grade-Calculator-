import 'dart:io';
import 'package:flutter/foundation.dart';
import 'package:test/test.dart';
import 'package:excel/excel.dart';
import 'package:grade_calculator/student_processor.dart';

void main() {
  test('Full Process: Read input, calculate, and write output', () async {
    // 1. Arrange: Create a dummy input Excel file
    var excel = Excel.createExcel();
    var sheet = excel['Sheet1'];

    // Add Header Row
    sheet.appendRow([
      TextCellValue('Name'),
      TextCellValue('Mat'),
      TextCellValue('Marks'),
    ]);

    // Add Student Data (Row 1)
    sheet.appendRow([
      TextCellValue('John'),
      TextCellValue('FE20A'),
      IntCellValue(85), // Use IntCellValue for better realism
    ]);

    final inputFile = File('test_input.xlsx');
    inputFile.writeAsBytesSync(excel.encode()!);

    // 2. Act: Run your main processing function
    await processExcelFile('test_input.xlsx', 'test_output.xlsx');

    // 3. Assert: Check if output file exists and has the correct grade
    final outputFile = File('test_output.xlsx');
    expect(
      outputFile.existsSync(),
      isTrue,
      reason: "Output file was not created.",
    );

    final outExcel = Excel.decodeBytes(outputFile.readAsBytesSync());
    final outSheet = outExcel.tables['Sheet1'];

    // Row 1 (John), Column 3 (Grade)
    // .value retrieves the CellValue object, .toString() gets the actual "A"
    var gradeValue = outSheet?.rows[1][3]?.value.toString();

    print(
      "DEBUG: Extracted Grade is '$gradeValue'",
    ); // Helps you see the result in console
    expect(
      gradeValue,
      equals('A'),
      reason: "Grade calculation or file writing failed.",
    );

    // 4. Cleanup: Delete temporary test files after verification
    if (inputFile.existsSync()) inputFile.deleteSync();
    if (outputFile.existsSync()) outputFile.deleteSync();
  });
}
