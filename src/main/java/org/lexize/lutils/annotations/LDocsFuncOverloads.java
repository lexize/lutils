package org.lexize.lutils.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface LDocsFuncOverloads {
    LDocsFuncOverload[] value();
}
