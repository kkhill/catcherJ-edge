package com.kkhill.core.thing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Service {

    private String name;
    private String description;
    private Method method;

    public Service(String name, String description, Method method) {
        this.name = name;
        this.description = description;
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Method getMethod() {
        return method;
    }

    public Object invoke(Object thing, Object... args) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(thing, args);
    }
}
