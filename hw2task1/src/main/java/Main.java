import java.lang.reflect.InvocationTargetException;

public class Main {

  public static void main(String[] args)
      throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    A a = new A();
    A cachedA = (A) Utils.cache(a);

    cachedA.setValue("Hello");
    System.out.println(cachedA.mainMethod());
    // Ожидается: "original method" и "Hello"
    System.out.println(cachedA.mainMethod());
    // Не выводит "original method", ожидается кэшированный "Hello"

    AFinal af = new AFinal("not_empty_string");
    AInterface cachedAFinal = (AInterface) Utils.cache(af);

    cachedAFinal.setValue("Hello");
    System.out.println(cachedAFinal.mainMethod());
    // Ожидается: "original method" и "Hello"
    System.out.println(cachedAFinal.mainMethod());
    // Не выводит "original method", ожидается кэшированный "Hello"
  }
}
