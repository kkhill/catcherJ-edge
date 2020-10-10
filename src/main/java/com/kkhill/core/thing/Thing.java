package com.kkhill.core.thing;

import com.kkhill.core.exception.IllegalThingException;

import java.util.HashMap;
import java.util.Map;

public abstract class Thing {

    /** id is unique, should be generated by core, rather driver **/
    private String id;

    /** different things can share a friendly name **/
    private String friendlyName;
    private boolean available;
    private State state;
    private Map<String, Property> properties;
    private Map<String, Service> services;

    public Thing(String friendlyName, boolean available) {

        this.friendlyName = friendlyName;
        this.available = available;
        this.properties = new HashMap<>();
        this.services = new HashMap<>();
    }

    public String getID() {
        return this.id;
    }

    public void setID(String id) {
        this.id = id;
    }


    public String getFriendlyName() {
        return this.friendlyName;
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
}
