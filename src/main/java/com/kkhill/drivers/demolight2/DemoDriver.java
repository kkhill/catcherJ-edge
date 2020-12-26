package com.kkhill.drivers.demolight2;

import com.kkhill.core.Catcher;
import com.kkhill.core.exception.IllegalThingException;
import com.kkhill.core.plugin.Driver;
import com.kkhill.drivers.demolight2.thing.Light;
import com.kkhill.common.thing.ThingType;

import java.util.Map;

public class DemoDriver extends Driver {

    /**
     *
     * @param data information read from drivers.yaml
     * @return configuration that will be used in initialize()
     */
    @Override
    public Object discover(Object data) {
        // TODO insert your code
        return data;
    }

    /**
     *
     * @param config configuration that
     */
    @Override
    @SuppressWarnings("unchecked")
    public void initialize(Object config) {

        Map<String, Object> configData = (Map<String, Object>) config;
        String ip = (String)configData.get("ip");
        int port = (int)configData.get("port");
        String name = (String) configData.get("name");

        // add things to core system.
        Light light = new Light(ThingType.LIGHT, name, "a light", ip, String.valueOf(port));
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
