package com.kkhill.drivers.demolight1.thing;

import com.kkhill.core.annotation.ServiceParam;
import com.kkhill.utils.thing.PropertyName;
import com.kkhill.utils.thing.ServiceName;
import com.kkhill.utils.thing.StateName;
import com.kkhill.core.Catcher;
import com.kkhill.core.exception.IllegalThingException;
import com.kkhill.core.exception.NotFoundException;
import com.kkhill.core.thing.Thing;
import com.kkhill.core.annotation.Property;
import com.kkhill.core.annotation.Service;
import com.kkhill.core.annotation.State;
import com.kkhill.drivers.demolight1.lib.Client;


public class Light extends Thing {

    private Client client;

    @State(description = "state")
    public String state;

    @Property(name="vendor", description = "vendor name")
    public String vendor = "otcaix";

    @Property(name= PropertyName.BRIGHTNESS, description = "brightness")
    public int brightness = 60;

    @Property(name=PropertyName.TEMPERATURE, description = "temperature")
    public int temperature;

    @Service(name="open", description = "open the light")
    public void open() {
        if(this.client.open()) {
            this.state = "on";
            try {
                Catcher.getThingMonitor().updateStateAndNotify(this.getId());
            } catch (NotFoundException | IllegalThingException e) {
                e.printStackTrace();
            }
        }
    }

    @Service(name= ServiceName.CLOSE, description = "close the light")
    public void close() {
        if(this.client.close()) {
            this.state = "off";
            try {
                Catcher.getThingMonitor().updateStateAndNotify(this.getId());
            } catch (NotFoundException | IllegalThingException e) {
                e.printStackTrace();
            }
        }
    }

    @Service(name=ServiceName.TOGGLE, description = "toggle the light")
    public void toggle() {
//        if(this.client.state()) {
//            if(this.client.close()) {
//                try {
//                    Catcher.getThingMonitor().updateAndNotifyState(this.getId(),false );
//                } catch (ThingNotFoundException | IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//        } else {
//            if(this.client.open()) {
//                try {
//                    Catcher.getThingMonitor().updateAndNotifyState(this.getId(), true);
//                } catch (ThingNotFoundException | IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

    }

    @Service(name="set_brightness", description = "decease brightness")
    public void setBrightness(@ServiceParam(name="brightness") int brightness) {
        if(this.client.setBrightness(brightness)) {
            this.brightness = brightness;
            try {
                Catcher.getThingMonitor().updatePropertyAndNotify(this.getId(), "brightness");
            } catch (NotFoundException | IllegalThingException e) {
                e.printStackTrace();
            }
        }
    }

    @Service(name="set_brightness_and_temperature", description = "decease brightness")
    public void setBandT(int brightness, int temperature) {
        if(this.client.setBrightness(brightness)) {
            this.brightness = brightness;
            try {
                Catcher.getThingMonitor().updatePropertyAndNotify(this.getId(), "brightness");
            } catch (NotFoundException  | IllegalThingException e) {
                e.printStackTrace();
            }
        }

        if(this.client.setTemperature(temperature)) {
            this.temperature = temperature;
            try {
                Catcher.getThingMonitor().updatePropertyAndNotify(this.getId(), "temperature");
            } catch (NotFoundException | IllegalThingException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *  polling for new state and property
     */
    @Service(name="update", description = "update data", poll = true, pollInternal = 10)
    public void update() {
        this.state = this.client.state() ? StateName.ON : StateName.OFF;
        this.brightness = this.client.getBrightness();
        this.temperature = this.client.getTemperature();
        try {
            Catcher.getThingMonitor().updateStateAndNotify(this.getId());
            Catcher.getThingMonitor().updatePropertyAndNotify(this.getId(), "brightness");
            Catcher.getThingMonitor().updatePropertyAndNotify(this.getId(), "temperature");
        } catch (NotFoundException | IllegalThingException  e) {
            e.printStackTrace();
        }
    }

    public Light(String type, String name, String description, String ip, String port) {
        super(type, name, description);
        this.client = new Client(ip, port);
        // TODO: test rule engine
//        Catcher.getScheduler().getExecutor().schedule(this::open, 5, TimeUnit.SECONDS);
    }
}
