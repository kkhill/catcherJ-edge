package com.kkhill.addons.automation.condition;

public abstract class Condition {

    protected String thing;
    protected String description;

    public abstract boolean check();

    public Condition(String thing, String description) {
        this.thing = thing;
        this.description = description;
    }

    public String getThing() {
        return thing;
    }

    public String getDescription() {
        if(description == null || "".equals(description)) return this.toString();
        else return description;
    }

    @Override
    public String toString() {
        return "unknown condition";
    }
}
