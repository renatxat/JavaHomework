package hw2task1;

public interface AInterface {

  String mainMethod();

  void setValue(String value);

  default void defaultMethod() {
    System.out.println("original method");
  }
}
