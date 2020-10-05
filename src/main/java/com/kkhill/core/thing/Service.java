package com.kkhill.core.thing;

import java.lang.reflect.Method;

public class Service {

    private String name;
    private String description;
    private Method method;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object invoke(){return null;}
}
