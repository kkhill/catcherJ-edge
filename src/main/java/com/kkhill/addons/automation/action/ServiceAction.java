package com.kkhill.addons.automation.action;

import com.kkhill.core.Catcher;
import com.kkhill.core.exception.NotFoundException;

public class ServiceAction extends Action {

    private String name;

    public ServiceAction(String name, String thing, String description) {
        super(thing, description);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getThing() {
        return thing;
    }

    @Override
    public void execute() {
        try {
            Catcher.getThingMonitor().callServiceAndNotify(thing, name, null);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return thing+"."+name;
    }
}
