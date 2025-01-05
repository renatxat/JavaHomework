import static net.bytebuddy.matcher.ElementMatchers.isAnnotatedWith;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import javax.annotation.processing.SupportedAnnotationTypes;
import java.lang.reflect.InvocationTargetException;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import java.lang.reflect.Proxy;


@SupportedAnnotationTypes({"Cache", "Setter"})
public class Utils {

  /**
   * @param obj Любой объект.
   * @return Объект класса Object, в котором переопределены методы, помеченные аннотациями
   * {@link Cache} и {@link Setter}. Если объект принадлежит final-классу или не имеет конструктора
   * по умолчанию, то все методы, реализованные в нём из интерфейсов и помеченные соответствующей
   * аннотацией в реализации (не интерфейсе) заменяются при помощи проксирования (Proxy). В ином же
   * случае для изменения методов объекта в Runtime используется библиотека
   * @see <a href="https://javadoc.io/doc/net.bytebuddy/byte-buddy/latest/index.html">ByteBuddy</a>,
   * изменяются как унаследованные методы из интерфейсов/родителей, так и методы самого класса.
   * <p>
   * Работает только для публичных классов.
   */
  public static Object cache(Object obj)
      throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    Class<?> clazz = obj.getClass();

    boolean hasDefaultConstructor = false;
    for (Constructor<?> constructor : clazz.getConstructors()) {
      // Проверяем, является ли конструктор дефолтным
      if (constructor.getParameterCount() == 0) {
        hasDefaultConstructor = true;
        break;
      }
    }

    if (Modifier.isFinal(clazz.getModifiers()) || !hasDefaultConstructor) {
      return Proxy.newProxyInstance(
          clazz.getClassLoader(),
          clazz.getInterfaces(),
          new CacheInvocationHandler(obj));
    }

    CacheInterceptor commonCacheInterceptor = new CacheInterceptor(obj);
    return new ByteBuddy()
        .subclass(clazz)
        .method(isAnnotatedWith(Cache.class)
            .or(isAnnotatedWith(Setter.class)))
        .intercept(MethodDelegation.to(commonCacheInterceptor))
        .make()
        .load(clazz.getClassLoader())
        .getLoaded()
        .getDeclaredConstructor()
        .newInstance();
  }
}
