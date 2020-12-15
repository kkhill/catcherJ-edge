package com.kkhill.core.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {

    String name();
    String description() default "";
    boolean poll() default false;
    boolean push() default false;
    int pollingInternal() default com.kkhill.core.thing.Service.DEFAULT_POLL_INTERNAL;
}
