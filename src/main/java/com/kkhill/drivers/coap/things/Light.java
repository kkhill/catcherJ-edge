package com.kkhill.drivers.coap.things;

import com.kkhill.common.thing.CProperty;
import com.kkhill.common.thing.CService;
import com.kkhill.common.thing.CState;
import com.kkhill.core.Catcher;
import com.kkhill.core.annotation.Property;
import com.kkhill.core.annotation.Service;
import com.kkhill.core.annotation.ServiceParam;
import com.kkhill.core.annotation.State;
import com.kkhill.core.exception.IllegalThingException;
import com.kkhill.core.exception.NotFoundException;
import com.kkhill.core.thing.Thing;
import com.kkhill.drivers.coap.lib.Client;

public class Light extends Thing {

    private Client client;

    @State(description = "state")
    public String state = CState.OFF;

    @Property(name = "vendor", description = "vendor name")
    public String vendor = "otcaix";

    @Property(name = CProperty.BRIGHTNESS, description = "brightness", unitOfMeasurement="ratio")
    public int brightness;

    @Service(name = CService.OPEN, description = "open the light")
    public void open() {

        this.client.send("state", "turn_on", "PUT");
        this.state = CState.ON;
        this.brightness = 100;
        try {
            Catcher.getThingMonitor().updateStateAndNotify(this.getId());
            Catcher.getThingMonitor().updatePropertyAndNotify(this.getId(), "brightness");
        } catch (NotFoundException | IllegalThingException e) {
            e.printStackTrace();
        }


    }

    @Service(name = CService.CLOSE, description = "close the light")
    public void close() {

        this.client.send("state", "turn_off", "PUT");
        this.state = CState.OFF;
        this.brightness = 0;
        try {
            Catcher.getThingMonitor().updateStateAndNotify(this.getId());
            Catcher.getThingMonitor().updatePropertyAndNotify(this.getId(), "brightness");
        } catch (NotFoundException | IllegalThingException e) {
            e.printStackTrace();
        }
    }

    @Service(name = "set_brightness", description = "set brightness")
    public void setBrightness(@ServiceParam(name = "brightness", description = "brightness") int brightness) {
        this.client.send("brightness", String.valueOf(brightness), "PUT");
        this.brightness = brightness;
        try {
            Catcher.getThingMonitor().updatePropertyAndNotify(this.getId(), "brightness");
        } catch (NotFoundException | IllegalThingException e) {
            e.printStackTrace();
        }
    }

    /**
     * polling for new state and property
     */
    @Service(name = "update", description = "update data", poll = true)
    public void update() {
        this.state = this.client.send("state", null, "GET");
        this.brightness = Integer.parseInt(this.client.send(
                "brightness", null, "GET"));
    }

    public Light(String type, String name, String description, String ip, String port) {
        super(type, name, description);
        this.client = new Client(ip, port);
        this.update();
    }
}

