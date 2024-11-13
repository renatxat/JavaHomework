interface AInterface {

  @Cache
  String method();

  @Setter
  void setValue(String value);
}

class A implements AInterface {

  private String value;

  @Override
  public String method() {
    System.out.println("original method");
    return value;
  }

  @Override
  public void setValue(String value) {
    this.value = value;
  }
}


public class Main {

  public static void main(String[] args) {
    AInterface a = new A();
    AInterface cachedA = Utils.cache(a);

    cachedA.setValue("Hello");
    System.out.println(cachedA.method());
    // Ожидается: "original method" и "Hello"

    System.out.println(
        cachedA.method());
    // Не выводит "original method", ожидается кэшированный "Hello"

    cachedA.setValue("World");
    System.out.println(cachedA.method());
    // Ожидается "original method" и "World"
    System.out.println(cachedA.method());
    // Не выводит "original method", ожидается кэшированный "World"
  }
}
