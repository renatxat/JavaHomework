import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Класс, реализующий кэширование результатов методов, помеченных аннотацией {@link Cache}, и сброс
 * кэширования при использовании методов, помеченных аннотацией {@link Setter}. Работает только для
 * классов, унаследованных от интерфейсов. В реализации перехвата методов используется
 * {@link CacheInterceptor}. Используется для работы с final классами и классами без дефолтного
 * конструктора, так имеет сильные ограничения на методы, для которых может использоваться.
 */
public class CacheInvocationHandler implements InvocationHandler {

  CacheInterceptor cacheInterceptor;

  public CacheInvocationHandler(Object originalObject) {
    this.cacheInterceptor = new CacheInterceptor(originalObject);
  }

  /**
   * @param proxy  the proxy instance that the method was invoked on
   * @param method the {@code Method} instance corresponding to the interface method invoked on the
   *               proxy instance.  The declaring class of the {@code Method} object will be the
   *               interface that the method was declared in, which may be a superinterface of the
   *               proxy interface that the proxy class inherits the method through.
   * @param args   an array of objects containing the values of the arguments passed in the method
   *               invocation on the proxy instance, or {@code null} if interface method takes no
   *               arguments. Arguments of primitive types are wrapped in instances of the
   *               appropriate primitive wrapper class, such as {@code java.lang.Integer} or
   *               {@code java.lang.Boolean}.
   * @return Изменяет только методы, унаследованные из интерфейсов и помеченные соответствующей
   * аннотацией в реализации. Просто повторяет логику {@link CacheInterceptor#intercept}.
   * <p>
   * Так как заменяет не все методы класса, а только унаследованные из интерфейса, то используется
   * только в особых ситуациях, когда это действительно необходимо. А именно, когда класс является
   * final, и когда у него отсутствует дефолтный конструктор.
   */
  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    // Получаем оригинальный метод, а не метод из интерфейса
    Method originalMethod = cacheInterceptor.getClassObject()
        .getMethod(method.getName(), method.getParameterTypes());
    return cacheInterceptor.intercept(null, originalMethod, args, null, null);
  }
}
