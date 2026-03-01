import 'package:test/test.dart';
import 'package:grade_calculator/student_processor.dart';

void main() {
  group('SafeListAccess Extension Tests', () {
    test('Returns element when index is valid', () {
      final list = <String?>['A', 'B'];

      final result = list.elementAtSafe(1);
      expect(result, equals('B'));
    });

    test('Returns null when index is out of bounds (positive)', () {
      final list = <String?>['A'];

      final result = list.elementAtSafe(5);
      expect(result, isNull);
    });

    test('Returns null when index is negative', () {
      final list = <String?>['A'];

      final result = list.elementAtSafe(-1);
      expect(result, isNull);
    });

    test('Returns null for empty list', () {
      final list = <String?>[];

      final result = list.elementAtSafe(0);
      expect(result, isNull);
    });
  });
}
