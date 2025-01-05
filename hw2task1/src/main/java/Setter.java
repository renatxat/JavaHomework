import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Методы, помеченные данной аннотацией, могут менять состояние объекта и влиять на результаты
 * вызовов его методов. Вызов любого соответствующего метода сбрасывает все кэшированные результаты
 * методов, помеченных аннотацией {@link Cache}.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Setter {

}
