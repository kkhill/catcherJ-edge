package com.kkhill.addons.rulengine.action;

import com.kkhill.addons.rulengine.action.Action;

public class ServiceAction extends Action {

    private String name;
    private String thing;

    public ServiceAction(String serviceType, String name, String thing) {
        super(serviceType);
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
