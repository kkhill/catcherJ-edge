package com.kkhill.driver.demo.thing;

import com.kkhill.common.convention.PropertyName;
import com.kkhill.common.convention.ServiceName;
import com.kkhill.core.Catcher;
import com.kkhill.core.thing.exception.PropertyNotFoundException;
import com.kkhill.core.thing.exception.ThingNotFoundException;
import com.kkhill.core.thing.Thing;
import com.kkhill.core.thing.annotation.Property;
import com.kkhill.core.thing.annotation.Service;
import com.kkhill.core.thing.annotation.State;
import com.kkhill.driver.demo.lib.Client;

public class Light extends Thing {

    private Client client;

    @State(description = "state")
    private boolean state;

    @Property(name="vendor", description = "vendor name")
    private String vendor = "otcaix";

    @Property(name= PropertyName.BRIGHTNESS, description = "brightness")
    private int brightness;

    @Property(name=PropertyName.TEMPERATURE, description = "temperature")
    private int temperature;

    @Service(name="open", description = "open the light")
    public void open() {
        if(this.client.open()) {
            try {
                Catcher.getThingMonitor().updateAndNotifyState(this.getID(), true);
            } catch (ThingNotFoundException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Service(name= ServiceName.CLOSE, description = "close the light")
    public void close() {
        if(this.client.close()) {
            try {
                Catcher.getThingMonitor().updateAndNotifyState(this.getID(), false);
            } catch (ThingNotFoundException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Service(name=ServiceName.TOGGLE, description = "toggle the light")
    public void toggle() {
        if(this.client.state()) {
            if(this.client.close()) {
                try {
                    Catcher.getThingMonitor().updateAndNotifyState(this.getID(),false );
                } catch (ThingNotFoundException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if(this.client.open()) {
                try {
                    Catcher.getThingMonitor().updateAndNotifyState(this.getID(), true);
                } catch (ThingNotFoundException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Service(name="set_brightness", description = "decease brightness")
    public void setBrightness(int brightness) {
        if(this.client.setBrightness(brightness)) {
            this.brightness = brightness;
            try {
                Catcher.getThingMonitor().updateAndNotifyProperty(this.getID(), "brightness", brightness);
            } catch (ThingNotFoundException | PropertyNotFoundException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Service(name="set_brightness_and_temperature", description = "decease brightness")
    public void setBandT(int brightness, int temperature) {
        if(this.client.setBrightness(brightness)) {
            this.brightness = brightness;
            try {
                Catcher.getThingMonitor().updateAndNotifyProperty(this.getID(), "brightness", brightness);
            } catch (ThingNotFoundException | PropertyNotFoundException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if(this.client.setTemperature(temperature)) {
            this.temperature = temperature;
            try {
                Catcher.getThingMonitor().updateAndNotifyProperty(this.getID(), "temperature", temperature);
            } catch (ThingNotFoundException | PropertyNotFoundException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public Light(String friendlyName, boolean available, Client client) {
        super(friendlyName, available);
        this.client = client;
    }
}
