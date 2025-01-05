public interface AInterface {

  String mainMethod();

  void setValue(String value);

  default void defaultMethod() {
    System.out.println("original method");
  }
}
