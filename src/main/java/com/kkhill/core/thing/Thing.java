package com.kkhill.core.thing;

import com.kkhill.core.Catcher;
import com.kkhill.core.exception.IllegalThingException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public abstract class Thing {

    /** id is unique and repeatable **/
    private String id;
    /** type can be used to group things **/
    private String type;
    /** services of unavailable things will not be called **/
    private boolean available;
    /** human-readable **/
    private String name;
    /** human-readable **/
    private String description;
    /** basic elements of thing, state, property and service **/
    private State state;
    private Map<String, Property> properties;
    private Map<String, Service> services;

    public Thing(String type, String name, String description) {
        this.type = type;
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFriendlyName() {
        return this.name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean isAvailable() {
        return this.available;
    }

    public void disable() {
        this.available = false;
    }

    public void enable() {
        this.available = true;
    }

    public Map<String, Property> getProperties() {
        return this.properties;
    }

    public void addProperty(Property property) throws IllegalThingException {
        if(properties.containsKey(property.getName())) {
            throw new IllegalThingException(String.format(
                    "property: %s is duplicate", property.getName()));
        }
        this.properties.put(property.getName(), property);
    }

    public Property getProperty(String name) {
        return this.properties.get(name);
    }

    public Map<String, Service> getServices() {
        return this.services;
    }

    public void addService(Service service) throws IllegalThingException {
        if(services.containsKey(service.getName())) {
            throw new IllegalThingException(String.format(
                    "service: %s is duplicate", service.getName()));
        }
        this.services.put(service.getName(), service);
    }

    public Service getService(String name) {
        return this.services.get(name);
    }

    /**
     * @return parse annotations in a thing, and build it
     * @throws IllegalThingException
     */
    public String build() throws IllegalThingException {

        this.properties = new HashMap<>();
        this.services = new HashMap<>();
        // TODO: thing id should be unique and repeatable
        if(this.id == null) this.setId(UUID.randomUUID().toString().replace("-", ""));

        // extract state and properties of thing
        Field[] fields = this.getClass().getDeclaredFields();
        int stateNum = 0;
        for(Field field : fields) {
            field.setAccessible(true);
            com.kkhill.core.annotation.State s = field.getAnnotation(com.kkhill.core.annotation.State.class);
            com.kkhill.core.annotation.Property p = field.getAnnotation(com.kkhill.core.annotation.Property.class);
            // build state
            if(s != null) {
                stateNum ++;
                if(stateNum > 1) throw new IllegalThingException("a thing must have only one state field");
                State state = new State(s.description(), this, field);
                this.setState(state);
            }
            // build properties
            if(p != null) {
                Property property = new Property(p.name(), p.description(), p.unitOfMeasurement(), this, field);
                this.addProperty(property);
            }
        }
        if(stateNum == 0) throw new IllegalThingException("a thing must have one state field");

        // extract services and polling method of a thing
        Method[] methods = this.getClass().getDeclaredMethods();
        for(Method method : methods) {
            method.setAccessible(true);
            com.kkhill.core.annotation.Service s = method.getAnnotation(com.kkhill.core.annotation.Service.class);
            // build service
            if(s != null) {
                Service service = new Service(s.name(), s.description(), this, method, s.pollInternal());
                if(s.poll()) {
                    service.enablePolling();
                    service.setPollInternal(s.pollInternal());
                    // add to scheduler
                    Catcher.getScheduler().addPolledService(service);
                }
                List<ServiceParam> sps = new ArrayList<>();
                for(Parameter p : method.getParameters()) {
                    com.kkhill.core.annotation.ServiceParam sp = p.getAnnotation(com.kkhill.core.annotation.ServiceParam.class);
                    if(sp != null) {
                        sps.add(new ServiceParam(sp.name(), p.getType().getSimpleName(), sp.description()));
                    }
                }
                service.setParameters(sps);
                this.addService(service);
            }
        }
        return id;
    }
}
