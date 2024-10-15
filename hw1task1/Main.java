import java.util.Scanner;

public class Main {

  private static Student<Integer> student;

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    int choice;

    while (true) {
      System.out.println("Выберите действие (напишите цифру):");
      System.out.println("0. Выход из программы");
      System.out.println("1. Создать нового студента");
      System.out.println("2. Изменить имя студента");
      System.out.println("3. Добавить оценки студенту");
      System.out.println("4. Удалить оценку у студента");
      System.out.println("5. Вывести описание студента");
      System.out.println("6. Действие назад");
      System.out.println("7. Действие вперёд");
      try {
        choice = Integer.parseInt(scanner.next());
      } catch (Exception exception) {
        System.out.println("Некорректный ввод");
        continue;
      }
      if (student == null && choice != 1) {
        System.out.println("Сначала создайте студента");
        continue;
      }
      switch (choice) {
        case 1 -> {
          System.out.println("Введите имя");
          student = new Student<>(scanner.next());
          System.out.println("Студент создан");
        }
        case 2 -> {
          System.out.println("Введите новое имя");
          student.set_name(scanner.next());
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
          if (student.action_back()) {
            System.out.println(student);
          } else {
            System.out.println("нечего отменять");
          }
        }
        case 7 -> {
          if (student.action_forward()) {
            System.out.println(student);
          } else {
            System.out.println("нечего отменять");
          }
        }
        case 0 -> System.exit(0);
        default -> System.out.println("Некорректный ввод");
      }
    }
  }
}
