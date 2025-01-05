## Hw-reflection

[Ссылка на оригинальное условие задания](https://docs.google.com/document/d/1PX8RtOOvL1C4cdNWnf_jvqRZQMsppG2x/edit?usp=sharing&ouid=100858605223823752484&rtpof=true&sd=true)

TLDR: реализация кэширования результатов методов, помеченных аннотацией @Cache

Как запустить jar:
```shell
mvn clean package
target/hw2task1.jar
java -jar target/hw2task1-1.0.jar
```



Если при запуске проекта у вас падает в runtime с ошибкой доступа, то попробуйте сделать следующее:
Edit configuration (слева от кнопки запуска) -> Modify options -> VM options:
```
--add-exports=java.base/sun.nio.ch=ALL-UNNAMED
--add-opens=java.base/java.lang=ALL-UNNAMED
--add-opens=java.base/java.lang.reflect=ALL-UNNAMED
--add-opens=java.base/java.io=ALL-UNNAMED
--add-exports=jdk.unsupported/sun.misc=ALL-UNNAMED
--add-exports=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED
--add-exports=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED
--add-exports=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED
```

MVP переопределения нескольких методов с bytebuddy:
```java
package hw2task1;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperMethod;
import net.bytebuddy.implementation.bind.annotation.This;

public class CacheInterceptor {

  @RuntimeType
  public Object intercept(
      @This Object self,
      @Origin Method method,
      @AllArguments Object[] args,
      @SuperMethod(nullIfImpossible = true) Method superMethod,
      @Empty Object defaultValue) throws Throwable {
  
    return "goal";
  }
}



import java.lang.reflect.Method;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Empty;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperMethod;
import net.bytebuddy.implementation.bind.annotation.This;

public class A {

  private String value;

  public A() {// crutch
    value = "";
  }

  @Setter
  public void setValue(String str) {
    value = str;
  }

  @Cache
  public String getValue() {
    System.out.println("original method");
    return value;
  }
}



import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;


public class Main {

  public static void main(String[] args) {
    try {
      A a = new ByteBuddy().subclass(A.class)
          .method(named("getValue"))
          .intercept(MethodDelegation.to(new MyInterceptor()))
          .make()
          .load(A.class.getClassLoader())
          .getLoaded()
          .getDeclaredConstructor()
          .newInstance();
      System.out.println(a.getValue());

    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }
}
```
