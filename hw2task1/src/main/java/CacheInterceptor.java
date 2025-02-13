import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Empty;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperMethod;
import net.bytebuddy.implementation.bind.annotation.This;

/**
 * Класс, реализующий кэширование результатов методов, помеченных аннотацией {@link Cache}, и сброс
 * кэширования при использовании методов, помеченных аннотацией {@link Setter}. Каждый метод
 * заменяется на intercept, который внутри себя вызывает оригинальный метод или возвращает его
 * кэшированный результат. Не работает с final классами и классами без дефолтного конструктора.
 */
public class CacheInterceptor {

  private final Object originalObject;
  private final HashMap<MethodAndArguments, Object> cache;

  private record MethodAndArguments(Method method, Object[] args) {

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      MethodAndArguments that = (MethodAndArguments) o;
      return Objects.equals(method, that.method) && Objects.deepEquals(args,
          that.args);
    }

    @Override
    public int hashCode() {
      return Objects.hash(method, Arrays.hashCode(args));
    }
  }

  public CacheInterceptor(Object originalObject) {
    this.originalObject = originalObject;
    cache = new HashMap<>();
  }

  public Class<?> getClassObject() {
    return originalObject.getClass();
  }

  @RuntimeType
  public Object intercept(
      @This Object self,
      @Origin Method method,
      @AllArguments Object[] args,
      @SuperMethod(nullIfImpossible = true) Method superMethod,
      @Empty Object defaultValue) throws Throwable {
    method.setAccessible(true);
    MethodAndArguments methodArgs = new MethodAndArguments(method, args);
    // Если метод помечен и @Setter и @Cache, то мы игнорируем Cache аннотацию
    if (method.isAnnotationPresent(Setter.class)) {
      cache.clear();
      return method.invoke(originalObject, args);
    }
    if (method.isAnnotationPresent(Cache.class)) {
      if (cache.containsKey(methodArgs)) {
        // Нужно, если метод вызывается не впервые
        return cache.get(methodArgs);
      }
      Object result = method.invoke(originalObject, args);
      cache.put(methodArgs, result);
      return result;
    }
    // Используется, если метод не помечен ни @Setter, ни @Cache
    // Такое может происходить при обработке методов изнутри CacheInvocationHandler
    return method.invoke(originalObject, args);
  }
}
