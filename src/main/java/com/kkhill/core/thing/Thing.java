package com.kkhill.core.thing;

import com.kkhill.core.Catcher;
import com.kkhill.core.exception.IllegalThingException;
import com.kkhill.core.thing.element.Property;
import com.kkhill.core.thing.element.Service;
import com.kkhill.core.thing.element.ServiceParam;
import com.kkhill.core.thing.element.State;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public abstract class Thing {

    /** id = plugin.pkg.name e.g. drivers.demolight1.hello
     * plugin.pkg is unique, and name should be unique in one plugin **/
    private String id;
    /** type can be used to group things **/
    private String type;
    /** not available means there are some problem with the thing, such like network error, device error, etc. **/
    private boolean available;
    /** human-readable, make sure name is unique in a driver **/
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
        this.available = true;
    }

    public Thing(String type, String name, String description, boolean available) {
        this(type, name, description);
        this.available = available;
    }

    public String getId() {
        return this.id;
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
        buildId();
        buildStateAndProperties();
        buildServices();
        return this.id;
    }

    public void buildId() {
        // TODO: last 2 field of class name
        String[] tmp = this.getClass().getName().split("\\.");
        this.id = tmp[2] + "." + tmp[3] + "." + this.name;
    }

    public void buildStateAndProperties() throws IllegalThingException {
        // extract state and properties of thing
        Field[] fields = this.getClass().getDeclaredFields();
        int stateNum = 0;
        for(Field field : fields) {
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
    }

    public void buildServices() throws IllegalThingException {

        Method[] methods = this.getClass().getDeclaredMethods();
        for(Method method : methods) {
            method.setAccessible(true);
            com.kkhill.core.annotation.Service s = method.getAnnotation(com.kkhill.core.annotation.Service.class);
            if(s == null) continue;
            // build service
            if(s.poll()) {
                if(method.getParameters().length!=0) throw new IllegalThingException("poll service should not have parameters");
                Service service = new Service(s.name(), s.description(), this, method, s.interval());
                // add to scheduler
                Catcher.getScheduler().addPolledService(service);
                this.addService(service);
            } else {
                List<ServiceParam> sps = new ArrayList<>();
                for(Parameter p : method.getParameters()) {
                    com.kkhill.core.annotation.ServiceParam sp = p.getAnnotation(com.kkhill.core.annotation.ServiceParam.class);
                    if(sp != null) {
                        sps.add(new ServiceParam(sp.name(), p.getType().getSimpleName(), sp.description()));
                    }
                }
                Service service = new Service(s.name(), s.description(), this, method, sps);
                this.addService(service);
            }
        }
    }
}
