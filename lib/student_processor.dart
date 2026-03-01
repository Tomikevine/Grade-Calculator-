import 'dart:io';
import 'package:excel/excel.dart';

/// Data Class representing a Student
class Student {
  final String name;
  final String matricule;
  final int? marks;
  final String grade;

  Student({required this.name, required this.matricule, this.marks})
    : grade = _calculateGrade(marks);

  /// Grade calculation logic
  static String _calculateGrade(int? score) {
    return switch (score) {
      null => 'F',
      >= 80 && <= 100 => 'A',
      >= 60 && < 80 => 'B',
      >= 50 && < 60 => 'C',
      >= 40 && < 50 => 'D',
      >= 0 && < 40 => 'F',
      _ => 'Invalid',
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
  final sheet = outputExcel['Sheet1'];

  // Header row
  sheet.appendRow([
    TextCellValue('Student Name'),
    TextCellValue('Matricule'),
    TextCellValue('Marks'),
    TextCellValue('Grade'),
  ]);

  for (var table in excel.tables.keys) {
    final rows = excel.tables[table]?.rows ?? [];

    // Skip header
    for (final row in rows.skip(1)) {
      final String name =
          row.elementAtSafe(0)?.value?.toString() ?? "Unknown Student";

      final String matricule = row.elementAtSafe(1)?.value?.toString() ?? "N/A";

      final String? rawMark = row.elementAtSafe(2)?.value?.toString();

      final int? marks = int.tryParse(rawMark ?? "");

      final student = Student(name: name, matricule: matricule, marks: marks);

      // Improved logic: do NOT force null marks to 0
      final CellValue markCell = student.marks != null
          ? IntCellValue(student.marks!)
          : TextCellValue("N/A");

      sheet.appendRow([
        TextCellValue(student.name),
        TextCellValue(student.matricule),
        markCell,
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

/// Safe list access extension
extension SafeListAccess<T> on List<T?> {
  T? elementAtSafe(int index) {
    if (index < 0 || index >= length) return null;
    return this[index];
  }
}
