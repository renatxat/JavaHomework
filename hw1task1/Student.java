import java.util.LinkedList;
import java.util.Objects;
import java.util.Stack;
import java.util.function.Predicate;

class Student<T> {

  private LinkedList<T> assessments;
  private String name;
  private final Predicate<T> assessment_validator;

  private Stack<HistoryEntry> history;
  private Stack<HistoryEntry> reverse_history;
  private Action current_action = Action.ActionOrdinary;

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
    assessments = new LinkedList<>();
    history = new Stack<>();
    reverse_history = new Stack<>();
    this.name = name;
    assessment_validator = validator != null ? validator : assessment -> true;
  }

  public Student(String name) {
    this(name, assessment -> true);
  }

  public Student(String name, T[] assessments, Predicate<T> validator) {
    this(name, validator);
    for (T assessment : assessments) {
      add(assessment);
    }
  }

  public Student(String name, T[] assessments) {
    this(name, assessments, assessment -> true);
  }


  protected void check_assessment(T assessment) {
    if (!assessment_validator.test(assessment)) {
      throw new IllegalArgumentException("Некорректная оценка: " + assessment);
    }
  }

  protected void check_index(int index) {
    if (index < 0 || index >= size()) {
      throw new IndexOutOfBoundsException(
          "Индекс " + index
              + " выходит за границы контейнера размера " + size());
    }
  }


  public int size() {
    return assessments.size();
  }

  public String get_name() {
    return name;
  }

  public void set_name(String s) {
    add_history(new HistoryEntry("set_name", name));
    name = s;
  }

  public T get(int index) {
    check_index(index);
    return assessments.get(index);
  }

  public void set(int index, T value) {
    check_index(index);
    check_assessment(value);
    add_history(new HistoryEntry("set", index, value));
    assessments.set(index, value);
  }


  public void add(int index, T assessment) {
    if (index != size()) {
      // для возможности добавления нового элемент в самый конец
      check_index(index);
    }
    check_assessment(assessment);
    assessments.add(index, assessment);
    add_history(new HistoryEntry("add", index));
  }

  public void add(T assessment) {
    add(size(), assessment);
  }

  public void remove(int index) {
    check_index(index);
    add_history(new HistoryEntry("remove", index, assessments.get(index)));
    assessments.remove(index);
  }

  public void remove() {
    remove(size() - 1);
  }


  private void add_history(HistoryEntry history_entry) {
    // действия отмены реализовано, как в браузерах, то есть
    // надо очищать reverse_history при обычном действии, а
    // при действии отмены надо добавлять его в history или reverse_history
    switch (current_action) {
      case Action.ActionOrdinary -> {
        history.add(history_entry);
        reverse_history.clear();
      }
      case Action.ActionBack -> reverse_history.add(history_entry);
      case Action.ActionForward -> history.add(history_entry);
    }
  }

  private boolean cancel_action() {
    var source = current_action == Action.ActionBack ? history : reverse_history;
    if (source.empty()) {
      current_action = Action.ActionOrdinary;
      return false;
    }
    HistoryEntry history_entry = source.pop();
    switch (history_entry.action) {
      case "set_name" -> set_name((String) history_entry.value);
      case "set" -> set(history_entry.index, (T) history_entry.value);
      case "add" -> remove(history_entry.index);
      case "remove" -> add(history_entry.index, (T) history_entry.value);
    }
    current_action = Action.ActionOrdinary;
    return true;
  }

  public boolean action_back() {
    current_action = Action.ActionBack;
    return cancel_action();
  }

  public boolean action_forward() {
    current_action = Action.ActionForward;
    return cancel_action();
  }


  @Override
  public String toString() {
    return name + ": " + assessments;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Student<?> student)) {
      return false;
    }
    return Objects.equals(assessments, student.assessments) && Objects.equals(
        name, student.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(assessments, name);
  }
}
