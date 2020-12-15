package com.kkhill.core.thing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Service {

    public static final int DEFAULT_POLL_INTERNAL = 30;

    // TODO: service parameters
    private String name;
    private String description;
    private Method method;
    private Thing thing;
    private boolean poll;
    private int pollingInternal = DEFAULT_POLL_INTERNAL;
    private boolean push;

    public Service(String name, String description, Thing thing, Method method) {
        this.name = name;
        this.description = description;
        this.method = method;
        this.thing = thing;
    }

    public Service(String name, String description, Thing thing, Method method, int internal) {
        this(name, description, thing, method);
        this.pollingInternal = internal;
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

    public int getPollingInternal() {
        return pollingInternal;
    }

    public void setPollingInternal(int internal) {
        this.pollingInternal = internal;
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
