import java.util.Arrays;
import java.util.Scanner;

public class Main {

  private static Student<Integer> student;

  public static void main(String[] args) {
//    test();
    try (Scanner scanner = new Scanner(System.in)) {
      while (true) {
        if (!takeStep(scanner)) {
          System.out.println("Работа программы завершена");
          System.exit(0);
        }
      }
    } catch (Exception e) {
      System.err.println("Произошла ошибка: " + e.getMessage());
    }
  }


  private static boolean takeStep(Scanner scanner) {
    System.out.println("Выберите действие (напишите цифру):");
    System.out.println("0. Выход из программы");
    System.out.println("1. Создать нового студента");
    System.out.println("2. Изменить имя студента");
    System.out.println("3. Добавить оценки студенту");
    System.out.println("4. Удалить оценку у студента");
    System.out.println("5. Вывести описание студента");
    System.out.println("6. Действие назад");
    System.out.println("7. Действие вперёд");
    int choice = Integer.parseInt(scanner.next());
    if (student == null && choice > 1 && choice <= 7) {
      System.out.println("Сначала создайте студента");
      return true;
    }
    switch (choice) {
      case 1 -> {
        System.out.println("Введите имя");
        student = new Student<>(scanner.next());
        System.out.println("Студент создан");
      }
      case 2 -> {
        System.out.println("Введите новое имя");
        student.setName(scanner.next());
        System.out.println("Имя изменено");
      }
      case 3 -> {
        System.out.println("Напишите оценки через пробел");
        scanner.nextLine();
        String line = scanner.nextLine();
        // sorry, I cannot fix this
        String[] arr = line.split(" ");
        for (String s : arr) {
          student.add(Integer.parseInt(s));
        }
        System.out.println("Оценки добавлены");
      }
      case 4 -> {
        System.out.println(student);
        System.out.println("Напишите значение, какой последней оценки вы хотите удалить");
        int value = scanner.nextInt();
        boolean check = false;
        for (int i = student.size() - 1; i >= 0; --i) {
          if (student.get(i) == value) {
            student.remove(i);
            System.out.println("Оценка удалена");
            check = true;
            break;
          }
        }
        if (!check) {
          System.out.println("Оценка не найдена");
        }
      }
      case 5 -> System.out.println(student);
      case 6 -> {
        if (student.undo()) {
          System.out.println(student);
        } else {
          System.out.println("Нечего отменять");
        }
      }
      case 7 -> {
        if (student.redo()) {
          System.out.println(student);
        } else {
          System.out.println("Нечего отменять");
        }
      }
      case 0 -> {
        return false;
      }
      default -> System.out.println("Не существует команды с таким номером");
    }
    return true;
  }

  @SuppressWarnings("unchecked")
  public static void test() {
    Student st_test = new Student("banana", Arrays.asList(1, 2, 3));
    System.out.println(st_test);
    st_test.setName("grew");
    System.out.println(st_test);
    st_test.redo();
    System.out.println(st_test);
    st_test.undo();
    System.out.println(st_test);
    st_test.undo();
    System.out.println(st_test);
    st_test.set(1, 4);
    System.out.println(st_test);
    st_test.undo();
    System.out.println(st_test);
    st_test.redo();
    System.out.println(st_test);
    st_test.add(7);
    System.out.println(st_test);
    st_test.undo();
    System.out.println(st_test);
    st_test.undo();
    System.out.println(st_test);
    st_test.undo();
    System.out.println(st_test);
    st_test.undo();
    System.out.println(st_test);
    st_test.undo();
/*
banana: [1, 2, 3]
grew: [1, 2, 3]
grew: [1, 2, 3]
banana: [1, 2, 3]
banana: [1, 2]
banana: [1, 4]
banana: [1, 2]
banana: [1, 4]
banana: [1, 4, 7]
banana: [1, 4]
banana: [1, 2]
banana: [1]
banana: []
*/
  }
}
