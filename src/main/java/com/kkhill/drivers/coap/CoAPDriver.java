package com.kkhill.drivers.coap;

import com.kkhill.common.thing.CThing;
import com.kkhill.core.Catcher;
import com.kkhill.core.exception.IllegalThingException;
import com.kkhill.core.plugin.Driver;
import com.kkhill.drivers.coap.things.Light;

import java.util.List;
import java.util.Map;

public class CoAPDriver extends Driver {
    @Override
    public Object discover(Object data) {
        return data;
    }

    @Override
    public void initialize(Object data) {
        List<Map<String, Object>> configData = (List<Map<String, Object>>) data;
        for(Map<String, Object> config : configData) {
            String ip = (String)config.get("ip");
            int port = (int)config.get("port");
            String name = (String) config.get("name");
            String description = (String) config.get("description");

            // add things to core system.
            Light light = new Light(CThing.LIGHT, name, description, ip, String.valueOf(port));
            try {
                Catcher.getThingMonitor().registerThing(light);
                light.update();
            } catch (IllegalThingException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void release() {

    }
}
