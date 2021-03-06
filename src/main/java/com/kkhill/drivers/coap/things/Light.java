package com.kkhill.drivers.coap.things;

import com.kkhill.common.thing.CProperty;
import com.kkhill.common.thing.CService;
import com.kkhill.common.thing.CState;
import com.kkhill.common.thing.CThing;
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

    @State(description = "状态")
    public String state = CState.OFFLINE;

    @Property(name = "vendor", description = "设备厂商")
    public String vendor = "zh2k3ang";

    @Property(name = CProperty.BRIGHTNESS, description = "亮度", unitOfMeasurement="ratio")
    public int brightness;

    @Service(name = CService.OPEN, description = "开灯")
    public void open() {

        String res = this.client.send("state", "turn_on", "PUT");
        if(res == null) return;
        this.state = CState.ON;
        this.brightness = 100;
        try {
            Catcher.getThingMonitor().updateStateAndNotify(this.getId());
            Catcher.getThingMonitor().updatePropertyAndNotify(this.getId(), "brightness");
        } catch (NotFoundException | IllegalThingException e) {
            e.printStackTrace();
        }


    }

    @Service(name = CService.CLOSE, description = "关灯")
    public void close() {
        String res = this.client.send("state", "turn_off", "PUT");
        if(res == null) return;
        this.state = CState.OFF;
        this.brightness = 0;
        try {
            Catcher.getThingMonitor().updateStateAndNotify(this.getId());
            Catcher.getThingMonitor().updatePropertyAndNotify(this.getId(), "brightness");
        } catch (NotFoundException | IllegalThingException e) {
            e.printStackTrace();
        }
    }

    @Service(name = "set_brightness", description = "设置亮度")
    public void setBrightness(@ServiceParam(name = "brightness", description = "亮度") int brightness) {
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
        try {
            String s = this.client.send("state", null, "GET");
            this.state = s == null ? CState.OFFLINE : s;
            Catcher.getThingMonitor().updateStateAndNotify(this.getId());

            String t = this.client.send("brightness", null, "GET");
            if(t != null) {
                this.brightness = Integer.parseInt(t);
                Catcher.getThingMonitor().updatePropertyAndNotify(this.getId(), "brightness");
            }
        } catch (NotFoundException | IllegalThingException e) {
            e.printStackTrace();
        }
    }

    public Light(String type, String name, String description, String ip, String port) {
        super(type, name, description);
        this.client = new Client(ip, port);
    }
}

