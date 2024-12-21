package hw2task1;

import javax.annotation.processing.SupportedAnnotationTypes;
import java.lang.reflect.InvocationTargetException;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

@SupportedAnnotationTypes({"Cache", "Setter"})
public class Utils {

  public static Object cache(Object obj)
      throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    CacheInterceptor commonCacheInterceptor = new CacheInterceptor(obj);

    return new ByteBuddy().subclass(obj.getClass()).method(
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
