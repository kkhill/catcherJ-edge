package com.kkhill.addons.automation.condition;

public abstract class Condition {

    protected String thing;
    protected String description;
    public abstract boolean check(Object data);

    public Condition(String thing, String description) {
        this.thing = thing;
        this.description = description;
    }

    public String getThing() {
        return thing;
    }

    public String getDescription() {
        return description;
    }
}
