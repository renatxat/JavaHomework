package hw2task1;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Методы, помеченные данной аннотацией, не исполняются повторно на одинаковых аргументах.
 * Их значения кэшируются и возвращаются при вызове.
 * Для получения объекта с кэшированием методов, необходимо обернуть его функцией {@link Utils#cache}.
 * При вызове методов, помеченных аннотацией {@link Setter}, все кэшированные результаты
 * стираются.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {

}
