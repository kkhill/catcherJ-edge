package com.kkhill.addons.automation.trigger;

public abstract class Trigger {
    protected String thing;
    protected String description;
    protected String event;

    public Trigger(String event, String thing, String description) {
        this.thing = thing;
        this.description = description;
        this.event = event;
    }

    public abstract boolean check(String thing, Object from, Object to);

    public String getThing() {
        return thing;
    }

    public String getEvent() {
        return event;
    }

    public String getDescription() {
        if(description == null || "".equals(description)) return this.toString();
        else return description;
    }
}
