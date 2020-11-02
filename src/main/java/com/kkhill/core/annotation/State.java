package com.kkhill.core.annotation;

import com.kkhill.common.convention.StateName;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface State {
    String description() default StateName.UNKNOWN;
}
