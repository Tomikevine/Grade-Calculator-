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
} 

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
} */

import 'package:test/test.dart';
import 'package:grade_calculator/student_processor.dart';

void main() {
  group('Grade Calculation Tests', () {

    test('Returns A for marks between 80 and 100', () {
      final student = Student(name: 'John', matricule: '001', marks: 85);
      expect(student.grade, equals('A'));
    });

    test('Returns B for marks between 60 and 79', () {
      final student = Student(name: 'Jane', matricule: '002', marks: 70);
      expect(student.grade, equals('B'));
    });

    test('Returns C for marks between 50 and 59', () {
      final student = Student(name: 'Mike', matricule: '003', marks: 55);
      expect(student.grade, equals('C'));
    });

    test('Returns D for marks between 40 and 49', () {
      final student = Student(name: 'Anna', matricule: '004', marks: 45);
      expect(student.grade, equals('D'));
    });

    test('Returns F for marks below 40', () {
      final student = Student(name: 'Paul', matricule: '005', marks: 20);
      expect(student.grade, equals('F'));
    });

    test('Returns F when marks are null', () {
      final student = Student(name: 'Null', matricule: '006');
      expect(student.grade, equals('F'));
    });

    test('Returns Invalid when marks > 100', () {
      final student = Student(name: 'Over', matricule: '007', marks: 150);
      expect(student.grade, equals('Invalid'));
    });

    test('Returns Invalid when marks < 0', () {
      final student = Student(name: 'Negative', matricule: '008', marks: -10);
      expect(student.grade, equals('Invalid'));
    });

  });
}
