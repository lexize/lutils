package org.lexize.lutils.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(LFields.class)
public @interface LField {
    String value();
    Class<?> type();
    String description() default "";
    boolean resource() default false;
}
