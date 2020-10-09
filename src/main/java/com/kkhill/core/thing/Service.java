package com.kkhill.core.thing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Service {

    private String name;
    private String description;
    private Method method;
    private Object thing;

    public Service(String name, String description, Object thing, Method method) {
        this.name = name;
        this.description = description;
        this.method = method;
        this.thing = thing;
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

    public Object invoke(Object... args) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(thing, args);
    }
}
