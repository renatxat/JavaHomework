package hw2task1;

import javax.annotation.processing.SupportedAnnotationTypes;
import java.lang.reflect.InvocationTargetException;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

@SupportedAnnotationTypes({"Cache", "Setter"})
public class Utils {

  /**
   * @param obj Любой объект обобщённого типа T.
   * @return Объект того же типа, в котором переопределены методы, помеченные аннотациями
   * {@link Cache} и {@link Setter}. Для изменения объекта в Runtime используется библиотека
   * @see <a href="https://javadoc.io/doc/net.bytebuddy/byte-buddy/latest/index.html">ByteBuddy</a>
   * TODO: Не работает для final классов.
   */
  public static <T> T cache(T obj)
      throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    CacheInterceptor commonCacheInterceptor = new CacheInterceptor(obj);

    return (T) new ByteBuddy().subclass(obj.getClass()).method(
            ElementMatchers.isAnnotatedWith(Cache.class)
                .or(ElementMatchers.isAnnotatedWith(Setter.class)))
        .intercept(MethodDelegation.to(commonCacheInterceptor))
        .make()
        .load(obj.getClass().getClassLoader())
        .getLoaded()
        .getDeclaredConstructor()
        .newInstance();
  }
}
