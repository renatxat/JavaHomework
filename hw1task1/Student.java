import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;
import java.util.function.Predicate;

class Student<T> {

  private LinkedList<T> grades;
  private String name;
  private final Predicate<T> gradeValidator;

  private Stack<HistoryEntry> history;
  private Stack<HistoryEntry> reverseHistory;
  private Action currentAction = Action.ActionOrdinary;

  public enum Action {
    ActionBack,
    ActionForward,
    ActionOrdinary
  }

  private static class HistoryEntry {

    String action;
    int index;
    Object value;

    HistoryEntry(String action, int index, Object value) {
      this.action = action;
      this.index = index;
      this.value = value;
    }

    HistoryEntry(String action, int index) {
      this.action = action;
      this.index = index;
    }

    HistoryEntry(String action, String str) {
      this.action = action;
      this.value = str;
      index = -1;
    }

  }


  public Student(String name, Predicate<T> validator) {
    grades = new LinkedList<>();
    history = new Stack<>();
    reverseHistory = new Stack<>();
    this.name = name;
    gradeValidator = validator != null ? validator : grade -> true;
  }

  public Student(String name) {
    this(name, grade -> true);
  }

  public Student(String name, ArrayList<T> grades, Predicate<T> validator) {
    this(name, validator);
    for (T grade : grades) {
      add(grade);
    }
  }

  public Student(String name, ArrayList<T> grades) {
    this(name, grades, grade -> true);
  }


  protected void checkGrade(T grade) {
    if (!gradeValidator.test(grade)) {
      throw new IllegalArgumentException("Некорректная оценка: " + grade);
    }
  }

  protected void checkIndex(int index) {
    if (index < 0 || index >= size()) {
      throw new IndexOutOfBoundsException(
          "Индекс " + index
              + " выходит за границы контейнера размера " + size());
    }
  }


  public int size() {
    return grades.size();
  }

  public String getName() {
    return name;
  }

  public void setName(String s) {
    addHistory(new HistoryEntry("setName", name));
    name = s;
  }

  public ArrayList<T> getGrades() {
    return new ArrayList<T>(grades);
  }

  public T get(int index) {
    checkIndex(index);
    return grades.get(index);
  }

  public void set(int index, T value) {
    checkIndex(index);
    checkGrade(value);
    addHistory(new HistoryEntry("set", index, value));
    grades.set(index, value);
  }


  public void add(int index, T grade) {
    if (index != size()) {
      // для возможности добавления нового элемент в самый конец
      checkIndex(index);
    }
    checkGrade(grade);
    grades.add(index, grade);
    addHistory(new HistoryEntry("add", index));
  }

  public void add(T grade) {
    add(size(), grade);
  }

  public void remove(int index) {
    checkIndex(index);
    addHistory(new HistoryEntry("remove", index, grades.get(index)));
    grades.remove(index);
  }

  public void remove() {
    remove(size() - 1);
  }


  private void addHistory(HistoryEntry historyEntry) {
    // действия отмены реализовано, как в браузерах, то есть
    // надо очищать reverseHistory при обычном действии, а
    // при действии отмены надо добавлять его в history или reverseHistory
    switch (currentAction) {
      case Action.ActionOrdinary -> {
        history.add(historyEntry);
        reverseHistory.clear();
      }
      case Action.ActionBack -> reverseHistory.add(historyEntry);
      case Action.ActionForward -> history.add(historyEntry);
    }
  }

  private boolean cancelAction() {
    var source = currentAction == Action.ActionBack ? history : reverseHistory;
    if (source.empty()) {
      currentAction = Action.ActionOrdinary;
      return false;
    }
    HistoryEntry historyEntry = source.pop();
    switch (historyEntry.action) {
      case "setName" -> setName((String) historyEntry.value);
      case "set" -> set(historyEntry.index, (T) historyEntry.value);
      case "add" -> remove(historyEntry.index);
      case "remove" -> add(historyEntry.index, (T) historyEntry.value);
    }
    currentAction = Action.ActionOrdinary;
    return true;
  }

  public boolean actionBack() {
    currentAction = Action.ActionBack;
    return cancelAction();
  }

  public boolean actionForward() {
    currentAction = Action.ActionForward;
    return cancelAction();
  }


  @Override
  public String toString() {
    return name + ": " + grades;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Student<?> student)) {
      return false;
    }
    return Objects.equals(grades, student.grades) && Objects.equals(
        name, student.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(grades, name);
  }
}
