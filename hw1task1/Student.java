import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;
import java.util.function.Predicate;

class Student<T> {

  private LinkedList<T> grades;
  private String name;
  private final Predicate<T> gradeValidator;
  private ActionManager actionManager;

  private enum Action {
    UNDO,
    REDO,
    ORDINARY
  }

  private static class ActionManager {

    private Stack<Runnable> history;
    private Stack<Runnable> reverseHistory;
    private Action status;

    ActionManager() {
      history = new Stack<>();
      reverseHistory = new Stack<>();
      status = Action.ORDINARY;
    }

    void push(Runnable action) {
      switch (status) {
        case Action.UNDO -> reverseHistory.push(action);
        case Action.REDO -> history.push(action);
        case Action.ORDINARY -> {
          history.push(action);
          reverseHistory.clear();
        }
      }
    }

    public void setStatus(Action status) {
      this.status = status;
    }

    public Action getStatus() {
      return status;
    }

    public Runnable get() {
      if (status == Action.UNDO && !history.empty()) {
        return history.pop();
      }
      if (status == Action.REDO && !reverseHistory.empty()) {
        return reverseHistory.pop();
      }
      return null;
    }
  }


  public Student(String name, Predicate<T> validator) {
    grades = new LinkedList<>();
    this.name = name;
    gradeValidator = validator != null ? validator : grade -> true;
    actionManager = new ActionManager();
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
    var oldName = name;
    // даже new String(name) не работает, так как он смотрит на поле при run
    actionManager.push(() -> setName(oldName));
    name = s;
  }

  public ArrayList<T> getGrades() {
    return new ArrayList<>(grades);
  }

  public T get(int index) {
    checkIndex(index);
    return grades.get(index);
  }

  public void set(int index, T value) {
    checkIndex(index);
    checkGrade(value);
    T oldValue = get(index);
    actionManager.push(() -> set(index, oldValue)); // просто get(index) не работает
    grades.set(index, value);
  }


  public void add(int index, T grade) {
    if (index != size()) {
      // для возможности добавления нового элемент в самый конец
      checkIndex(index);
    }
    checkGrade(grade);
    actionManager.push(() -> remove(index));
    grades.add(index, grade);
  }

  public void add(T grade) {
    add(size(), grade);
  }

  public void remove(int index) {
    checkIndex(index);
    T removedValue = grades.get(index);
    actionManager.push(() -> add(index, removedValue));
    grades.remove(index);
  }

  public void remove() {
    remove(size() - 1);
  }

  private boolean cancelAction(Action typeOfAction) {
    actionManager.setStatus(typeOfAction);
    Runnable action = actionManager.get();
    if (action == null) {
      return false;
    }
    action.run();
    actionManager.setStatus(Action.ORDINARY);
    return true;
  }

  public boolean undo() {
    return cancelAction(Action.UNDO);
  }

  public boolean redo() {
    return cancelAction(Action.REDO);
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
