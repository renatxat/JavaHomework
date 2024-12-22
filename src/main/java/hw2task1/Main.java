package hw2task1;

import java.lang.reflect.InvocationTargetException;

public class Main {

  public static void main(String[] args)
      throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    A a = new A();
    AInterface cachedA = (AInterface) Utils.cache(a);

    cachedA.setValue("Hello");
    System.out.println(cachedA.mainMethod());
    // Ожидается: "original method" и "Hello"
    System.out.println(cachedA.mainMethod());
    // Не выводит "original method", ожидается кэшированный "Hello"


    AFinal af = new AFinal("not_empty_string");
    cachedA = (AInterface) Utils.cache(af);

    cachedA.setValue("Hello");
    System.out.println(cachedA.mainMethod());
    // Ожидается: "original method" и "Hello"
    System.out.println(cachedA.mainMethod());
    // Не выводит "original method", ожидается кэшированный "Hello"
  }
}
