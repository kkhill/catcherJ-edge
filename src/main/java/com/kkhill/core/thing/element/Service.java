package com.kkhill.core.thing.element;

import com.kkhill.core.thing.Thing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class Service {

    /** human-readable **/
    private String name;
    /** human-readable **/
    private String description;
    /** real executor **/
    private Method method;
    /** instance to invoke method **/
    private Thing thing;
    /** parameters **/
    private List<ServiceParam> parameters;
    /** poll service **/
    private boolean poll;
    private int interval;
    /** push service **/
    private boolean push;

    public Service(String name, String description, Thing thing, Method method) {
        this.name = name;
        this.description = description;
        this.method = method;
        this.thing = thing;
    }

    public Service(String name, String description, Thing thing, Method method, List<ServiceParam> parameters) {
        this(name, description, thing, method);
        this.parameters = parameters;
    }

    public Service(String name, String description, Thing thing, Method method, int interval) {
        this(name, description, thing, method);
        this.interval = interval;
        this.poll = true;
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
     * @param params
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public Object call(Map<String, Object> params) throws InvocationTargetException, IllegalAccessException {

        // reorder args based on parameter name (parameters) that defined in @Service method
        Object[] args = new Object[parameters.size()];
        for(int i=0; i<parameters.size(); i++) {
            args[i] = params.get(parameters.get(i).getName());
        }
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

    public int getPollInterval() {
        return interval;
    }

    public void setPollInterval(int interval) {
        this.interval = interval;
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
