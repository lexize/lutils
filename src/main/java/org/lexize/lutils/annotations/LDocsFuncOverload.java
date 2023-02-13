package org.lexize.lutils.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface LDocsFuncOverload {
    Class<?>[] argumentTypes() default {};
    String[] argumentNames() default {};
    Class<?> returnType() default DEFAULT_TYPE.class;
    String description() default "";
    boolean resource() default false;

    final class DEFAULT_TYPE {}
}
