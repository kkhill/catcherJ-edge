package com.kkhill.driver.demo.thing;

import com.kkhill.common.convention.PropertyName;
import com.kkhill.common.convention.ServiceName;
import com.kkhill.common.convention.StateName;
import com.kkhill.core.Catcher;
import com.kkhill.core.exception.IllegalThingException;
import com.kkhill.core.exception.NotFoundException;
import com.kkhill.core.thing.Thing;
import com.kkhill.core.annotation.Property;
import com.kkhill.core.annotation.Service;
import com.kkhill.core.annotation.State;
import com.kkhill.driver.demo.lib.Client;

public class Light extends Thing {

    private Client client;

    @State(description = "state")
    private String state;

    @Property(name="vendor", description = "vendor name")
    private String vendor = "otcaix";

    @Property(name= PropertyName.BRIGHTNESS, description = "brightness")
    private int brightness;

    @Property(name=PropertyName.TEMPERATURE, description = "temperature")
    private int temperature;

    @Service(name="open", description = "open the light")
    public void open() {
        if(this.client.open()) {
            this.state = "on";
            try {
                Catcher.getThingMonitor().updateAndNotifyState(this.getID());
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
                Catcher.getThingMonitor().updateAndNotifyState(this.getID());
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
//                    Catcher.getThingMonitor().updateAndNotifyState(this.getID(),false );
//                } catch (ThingNotFoundException | IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//        } else {
//            if(this.client.open()) {
//                try {
//                    Catcher.getThingMonitor().updateAndNotifyState(this.getID(), true);
//                } catch (ThingNotFoundException | IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

    }

    @Service(name="set_brightness", description = "decease brightness")
    public void setBrightness(int brightness) {
        if(this.client.setBrightness(brightness)) {
            this.brightness = brightness;
            try {
                Catcher.getThingMonitor().updateAndNotifyProperty(this.getID(), "brightness");
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
                Catcher.getThingMonitor().updateAndNotifyProperty(this.getID(), "brightness");
            } catch (NotFoundException  | IllegalThingException e) {
                e.printStackTrace();
            }
        }

        if(this.client.setTemperature(temperature)) {
            this.temperature = temperature;
            try {
                Catcher.getThingMonitor().updateAndNotifyProperty(this.getID(), "temperature");
            } catch (NotFoundException | IllegalThingException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *  polling for new state and property
     */
    @Service(name="update", description = "update data", poll = true)
    public void update() {
        this.state = this.client.state() ? StateName.ON : StateName.OFF;
        this.brightness = this.client.getBrightness();
        this.temperature = this.client.getTemperature();
        try {
            Catcher.getThingMonitor().updateAndNotifyState(this.getID());
            Catcher.getThingMonitor().updateAndNotifyProperty(this.getID(), "brightness");
            Catcher.getThingMonitor().updateAndNotifyProperty(this.getID(), "temperature");
        } catch (NotFoundException | IllegalThingException  e) {
            e.printStackTrace();
        }

        System.out.println("i had been invoked...");
    }

    public Light(String friendlyName, boolean available, Client client) {
        super(friendlyName, available);
        this.client = client;
    }
}
