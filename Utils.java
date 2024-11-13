import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.processing.SupportedAnnotationTypes;

@SupportedAnnotationTypes({"Cache", "Setter"})
class Utils {

  public static <T> T cache(T obj) {
    return (T) Proxy.newProxyInstance(
        obj.getClass().getClassLoader(),
        obj.getClass().getInterfaces(),
        new CacheInvocationHandler(obj));
  }

  private static class CacheInvocationHandler implements InvocationHandler {

    private final Object originalObject;
    private HashMap<Method, Object> cache;
    private boolean isModified;

    public CacheInvocationHandler(Object originalObject) {
      this.originalObject = originalObject;
      cache = new HashMap<>();
      isModified = false;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      // Применяется и к унаследованным методам
      if (method.isAnnotationPresent(Cache.class)) {
        if (isModified) {
          isModified = false;
          cache.remove(method);
        }
        if (cache.containsKey(method)) {
          return cache.get(method);
          // Нужно, если метод вызывается не впервые
        }
        Object result = method.invoke(originalObject, args);
        cache.put(method, result);
        return result;
      }

      if (method.isAnnotationPresent(Setter.class)) {
        isModified = true;
      }
      return method.invoke(originalObject, args);
    }

  }
}
