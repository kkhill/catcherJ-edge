package com.kkhill.addons.automation.action;

import java.util.Map;

public abstract class Action {

    protected String thing;
    protected String description;
    protected Map<String, Object> params;

    public abstract void execute();

    public Action(String thing, String description, Map<String, Object> params) {
        this.thing = thing;
        this.description = description;
        this.params = params;
    }

    public String getThing() {
        return thing;
    }

    public String getDescription() {
        if(description == null || "".equals(description)) return this.toString();
        else return description;
    }

    public Map<String, Object> getParams() {
        return this.params;
    }

    @Override
    public String toString() {
        return "unknown action";
    }
}
