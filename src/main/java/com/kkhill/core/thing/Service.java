package com.kkhill.core.thing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class Service {

    public static final int DEFAULT_POLL_INTERNAL = 30;


    /** human-readable **/
    private String name;
    /** human-readable **/
    private String description;
    /** real executor **/
    private Method method;
    /** identity who's method **/
    private Thing thing;
    /** parameters **/
    private List<ServiceParam> parameters;
    /** poll service **/
    private boolean poll;
    private int pollInternal = DEFAULT_POLL_INTERNAL;
    /** push service **/
    private boolean push;

    public Service(String name, String description, Thing thing, Method method) {
        this.name = name;
        this.description = description;
        this.method = method;
        this.thing = thing;
    }

    public Service(String name, String description, Thing thing, Method method, int internal) {
        this(name, description, thing, method);
        this.pollInternal = internal;
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

    public List<ServiceParam> getParameters() {
        return parameters;
    }

    public void setParameters(List<ServiceParam> parameters) {
        this.parameters = parameters;
    }

    /**
     * call service, this method must be invoked by ThingMonitor to notify event
     * @param args
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    // TODO call service with parameter
    Object call(Object... args) throws InvocationTargetException, IllegalAccessException {
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

    public int getPollInternal() {
        return pollInternal;
    }

    public void setPollInternal(int internal) {
        this.pollInternal = internal;
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
