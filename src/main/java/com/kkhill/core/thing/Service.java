package com.kkhill.core.thing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Service {

    private String name;
    private String description;
    private Method method;
    private Thing thing;
    private boolean poll;
    private boolean push;

    public Service(String name, String description, Thing thing, Method method) {
        this.name = name;
        this.description = description;
        this.method = method;
        this.thing = thing;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Method getMethod() {
        return this.method;
    }

    public String getThingId() {
        return this.thing.getId();
    }

    /**
     * call service, this method must be invoked by ThingMonitor to notify event
     * @param args
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    Object invoke(Object... args) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(thing, args);
    }

    public boolean isPolled() {
        return this.poll;
    }

    public boolean isPushed() {
        return this.push;
    }

    public void enablePolling() {
        this.poll = true;
    }

    public void enablePushing() {
        this.push = true;
    }

    public void disablePolling() {
        this.poll = false;
    }

    public void disablePushing() {
        this.push = false;
    }

}
