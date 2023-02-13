package org.lexize.lutils.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Inherited
public @interface LDescription {
    String value();
    boolean resource() default false;
}
