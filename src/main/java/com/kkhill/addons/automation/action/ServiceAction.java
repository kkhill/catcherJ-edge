package com.kkhill.addons.automation.action;

import com.kkhill.core.Catcher;
import com.kkhill.core.exception.NotFoundException;

import java.util.Map;

public class ServiceAction extends Action {

    private String name;

    public ServiceAction(String name, String thing, String description, Map<String, Object> params) {
        super(thing, description, params);
        this.name = name;
    }

    public String getName() {
        return name;
    }


    @Override
    public void execute() {
        try {
            Catcher.getThingMonitor().callServiceAndNotify(thing, name, this.params);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return thing+"."+name;
    }
}
