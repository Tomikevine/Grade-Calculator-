// This is a basic Flutter widget test.
//
// To perform an interaction with a widget in your test, use the WidgetTester
// utility in the flutter_test package. For example, you can send tap and scroll
// gestures. You can also use WidgetTester to find child widgets in the widget
// tree, read text, and verify that the values of widget properties are correct.

/*import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';

import 'package:grade_calculator/main.dart';


void main() {
  testWidgets('Counter increments smoke test', (WidgetTester tester) async {
    // Build our app and trigger a frame.
    await tester.pumpWidget(const processExcelFile());

    // Verify that our counter starts at 0.
    expect(find.text('0'), findsOneWidget);
    expect(find.text('1'), findsNothing);

    // Tap the '+' icon and trigger a frame.
    await tester.tap(find.byIcon(Icons.add));
    await tester.pump();

    // Verify that our counter has incremented.
    expect(find.text('0'), findsNothing);
    expect(find.text('1'), findsOneWidget);
  });
} */

import 'package:test/test.dart';
import 'package:grade_calculator/main.dart';

void main() {
  group('Student Grade Logic Tests', () {
    test('Should assign A for marks >= 80', () {
      final student = Student(name: "Test", matricule: "001", marks: 85);
      expect(student.grade, equals('A'));
    });

    test('Should assign F for marks below 40', () {
      final student = Student(name: "Test", matricule: "002", marks: 35);
      expect(student.grade, equals('F'));
    });

    test('Should handle null marks as F', () {
      final student = Student(name: "Test", matricule: "003", marks: null);
      expect(student.grade, equals('F'));
    });

    test('Should return "Invalid" for marks over 100', () {
      final student = Student(name: "Test", matricule: "004", marks: 150);
      expect(student.grade, equals('Invalid'));
    });
  });
}
