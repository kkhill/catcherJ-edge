package com.kkhill.drivers.asynclight.thing;

import com.kkhill.core.Catcher;
import com.kkhill.core.annotation.Property;
import com.kkhill.core.annotation.Service;
import com.kkhill.core.annotation.State;
import com.kkhill.core.exception.IllegalThingException;
import com.kkhill.core.exception.NotFoundException;
import com.kkhill.core.thing.Thing;
import com.kkhill.drivers.demolight1.lib.Client;
import com.kkhill.common.thing.CService;

public class Light extends Thing {

    private Client client;

    @State(description = "state")
    private String state;

    @Property(name="vendor", description = "vendor name")
    private String vendor = "otcaix";

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

    @Service(name= CService.CLOSE, description = "close the light")
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

    /**
     *  polling for new state and property
     */


    public Light(String type, String name, String description, String ip, String port) {
        super(type, name, description);
        this.client = new Client(ip, port);
    }
}
