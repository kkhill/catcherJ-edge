package com.kkhill.addons.automation.action;

public abstract class Action {

    protected String thing;
    protected String description;

    public abstract void execute();

    public Action(String thing, String description) {
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
        return "unknown action";
    }
}
