package com.kkhill.drivers.coap;

import com.kkhill.common.thing.CThing;
import com.kkhill.core.Catcher;
import com.kkhill.core.exception.IllegalThingException;
import com.kkhill.core.plugin.Driver;
import com.kkhill.core.thing.Thing;
import com.kkhill.drivers.coap.things.*;

import java.util.List;
import java.util.Map;

public class CoAPDriver extends Driver {

    @Override
    public Object discover(Object data) {
        return data;
    }

    @Override
    public void initialize(Object data) {

        Map<String, Object> allDevices = (Map<String, Object>) data;
        for(String type : allDevices.keySet()) {
            List<Map<String, Object>> devices = (List<Map<String, Object>>) allDevices.get(type);

            for(Map<String, Object> device : devices) {
                String ip = (String)device.get("ip");
                int port = (int)device.get("port");
                String name = (String) device.get("name");
                String description = (String) device.get("description");

                Thing thing = null;
                if(CThing.LIGHT.equals(type)) {
                    thing = new Light(CThing.LIGHT, name, description, ip, String.valueOf(port));
                } else if(CThing.LOCK.equals(type)) {
                    thing = new Lock(CThing.LOCK, name, description, ip, String.valueOf(port));
                } else if(CThing.TV.equals(type)) {
                    thing = new Tv(CThing.TV, name, description, ip, String.valueOf(port));
                } else if(CThing.VACUUM.equals(type)) {
                    thing = new Vacuum(CThing.VACUUM, name, description, ip, String.valueOf(port));
                } else if(CThing.THERMOSTAT.equals(type)) {
                    thing = new Thermostat(CThing.THERMOSTAT, name, description, ip, String.valueOf(port));
                } else if(CThing.HEATER.equals(type)) {
                    thing = new Heater(CThing.HEATER, name, description, ip, String.valueOf(port));
                }


                if(thing != null) {
                    try {
                        Catcher.getThingMonitor().registerThing(thing);
                    } catch (IllegalThingException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    @Override
    public void release() {

    }
}
