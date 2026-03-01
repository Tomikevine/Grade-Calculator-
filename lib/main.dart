/*import 'dart:io';
import 'package:excel/excel.dart';

/// [Data Class] representation of a Student
class Student {
  final String name;
  final String matricule;
  final int? marks;
  final String grade;

  Student({required this.name, required this.matricule, this.marks})
    : grade = _calculateGrade(marks);

  /// Logic using Switch Expression (Dart's version of 'when')
  static String _calculateGrade(int? score) {
    // Handle null or edge cases using a switch expression
    return switch (score) {
      null => 'F',
      >= 80 && <= 100 => 'A',
      >= 60 && < 80 => 'B',
      >= 50 && < 60 => 'C',
      >= 40 && < 50 => 'D',
      >= 0 && < 40 => 'F',
      _ => 'Invalid', // Handles scores > 100 or < 0
    };
  }
}

 Future<void> processExcelFile(String inputPath, String outputPath) async {
  // your logic

  final file = File(inputPath);
  if (!file.existsSync()) {
    print("❌ Error: File not found at $inputPath");
    return;
  }

  final excel = Excel.decodeBytes(file.readAsBytesSync());
  final outputExcel = Excel.createExcel();
  final sheet = outputExcel['Sheet1'];

  // Define Header Row
  sheet.appendRow([
    TextCellValue('Student Name'),
    TextCellValue('Matricule'),
    TextCellValue('Marks'),
    TextCellValue('Grade'),
  ]);

  for (var table in excel.tables.keys) {
    final rows = excel.tables[table]?.rows ?? [];

    // Skip header (index 0), process data rows
    for (final row in rows.skip(1)) {
      // Safe calls (?.) and Elvis operators (??) to handle null/empty cells
      final String name =
          row.elementAtSafe(0)?.value?.toString() ?? "Unknown Student";
      final String matricule = row.elementAtSafe(1)?.value?.toString() ?? "N/A";

      // Attempt to parse marks; if null or non-numeric, result is null
      final String? rawMark = row.elementAtSafe(2)?.value?.toString();
      final int? marks = int.tryParse(rawMark ?? "");

      // Create instance of our Data Class
      final student = Student(name: name, matricule: matricule, marks: marks);

      // Append to output
      sheet.appendRow([
        TextCellValue(student.name),
        TextCellValue(student.matricule),
        IntCellValue(student.marks ?? 0), // Elvis: default to 0 for the sheet
        TextCellValue(student.grade),
      ]);
    }
  }

  final fileBytes = outputExcel.encode();
  if (fileBytes != null) {
    File(outputPath)
      ..createSync(recursive: true)
      ..writeAsBytesSync(fileBytes);
    print("🚀 Success: Processed file saved as $outputPath");
  }
}

extension on List<Data?> {
  elementAtSafe(int i) {}
}

void main() {
  processExcelFile("input.xlsx", "final_results.xlsx");
}



import 'dart:io';
import 'package:excel/excel.dart';

/// [Data Class] representation of a Student
class Student {
  final String name;
  final String matricule;
  final int? marks;
  final String grade;

  Student({required this.name, required this.matricule, this.marks})
      : grade = _calculateGrade(marks);

  /// Logic using Switch Expression (Dart's version of 'when')
  static String _calculateGrade(int? score) {
    // Handle null or edge cases using a switch expression
    return switch (score) {
      null => 'F',
      >= 80 && <= 100 => 'A',
      >= 60 && < 80 => 'B',
      >= 50 && < 60 => 'C',
      >= 40 && < 50 => 'D',
      >= 0 && < 40 => 'F',
      _ => 'Invalid', // Handles scores > 100 or < 0
    };
  }
}

Future<void> processExcelFile(String inputPath, String outputPath) async {
  final file = File(inputPath);
  if (!file.existsSync()) {
    print("❌ Error: File not found at $inputPath");
    return;
  }

  final excel = Excel.decodeBytes(file.readAsBytesSync());
  final outputExcel = Excel.createExcel();
  
  // Note: Modern excel package uses 'Sheet1' by default or creates it
  final sheet = outputExcel['Sheet1'];

  // Define Header Row
  sheet.appendRow([
    TextCellValue('Student Name'),
    TextCellValue('Matricule'),
    TextCellValue('Marks'),
    TextCellValue('Grade'),
  ]);

  for (var table in excel.tables.keys) {
    final rows = excel.tables[table]?.rows ?? [];

    // Skip header (index 0), process data rows
    for (final row in rows.skip(1)) {
      // Safe calls (?.) and Elvis operators (??) to handle null/empty cells
      final String name =
          row.elementAtSafe(0)?.value?.toString() ?? "Unknown Student";
      final String matricule = row.elementAtSafe(1)?.value?.toString() ?? "N/A";

      // Attempt to parse marks; if null or non-numeric, result is null
      final String? rawMark = row.elementAtSafe(2)?.value?.toString();
      final int? marks = int.tryParse(rawMark ?? "");

      // Create instance of our Data Class
      final student = Student(name: name, matricule: matricule, marks: marks);

      // Append to output
      sheet.appendRow([
        TextCellValue(student.name),
        TextCellValue(student.matricule),
        IntCellValue(student.marks ?? 0), // Elvis: default to 0 for the sheet
        TextCellValue(student.grade),
      ]);
    }
  }

  final fileBytes = outputExcel.encode();
  if (fileBytes != null) {
    File(outputPath)
      ..createSync(recursive: true)
      ..writeAsBytesSync(fileBytes);
    print("🚀 Success: Processed file saved as $outputPath");
  }
}

/// Fixed the Extension logic to actually return the element
extension SafeListAccess on List<Data?> {
  Data? elementAtSafe(int index) {
    if (index < 0 || index >= length) return null;
    return this[index];
  }
}

void main() async {
  // Added await here to ensure the process finishes before the app exits
  await processExcelFile("input.xlsx", "final_results.xlsx");
}  */

import 'student_processor.dart';

void main() async {
  await processExcelFile("input.xlsx", "final_results.xlsx");
}
