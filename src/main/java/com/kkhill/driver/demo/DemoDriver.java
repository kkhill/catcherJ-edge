package com.kkhill.driver.demo;

import com.kkhill.core.Catcher;
import com.kkhill.core.exception.IllegalThingException;
import com.kkhill.core.plugin.Driver;
import com.kkhill.driver.demo.lib.Client;
import com.kkhill.driver.demo.thing.Light;

public class DemoDriver extends Driver {


    @Override
    public void discover() {

    }

    @Override
    public void initialize() {
        Catcher.getPluginRegistry().registerPlugin("demo");
        // add things to core system.
        Light light = new Light("lovely", true, new Client());
        try {
            Catcher.getThingMonitor().registerThing(light);
        } catch (IllegalThingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void release() {

    }
}
