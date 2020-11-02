package com.kkhill.addon.rulengine.helper;

public class ServiceAction extends Action {

    private String name;
    private String thing;

    public ServiceAction(String name, String thing) {
        this.name = name;
        this.thing = thing;
    }

    public String getName() {
        return name;
    }

    public String getThing() {
        return thing;
    }
}
