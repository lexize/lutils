package org.lexize.lutils.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface LDocsFuncOverloads {
    LDocsFuncOverload[] value();
}
