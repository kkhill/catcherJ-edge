package com.kkhill.drivers.coap.things;

import com.kkhill.common.thing.CProperty;
import com.kkhill.common.thing.CService;
import com.kkhill.common.thing.CState;
import com.kkhill.core.Catcher;
import com.kkhill.core.annotation.Property;
import com.kkhill.core.annotation.Service;
import com.kkhill.core.annotation.State;
import com.kkhill.core.exception.IllegalThingException;
import com.kkhill.core.exception.NotFoundException;
import com.kkhill.core.thing.Thing;
import com.kkhill.drivers.coap.lib.Client;

public class Tv extends Thing {

    private Client client;

    @State(description = "state")
    public String state = CState.OFFLINE;

    @Property(name = "vendor", description = "设备厂商")
    public String vendor = "zh2k3ang";

    @Property(name = CProperty.VOLUME, description = "音量", unitOfMeasurement="int")
    public int volume;

    @Service(name = CService.OPEN, description = "打开电视")
    public void open() {

        String res = this.client.send("state", "open", "PUT");
        if(res == null) return;
        this.state = CState.ON;
        try {
            Catcher.getThingMonitor().updateStateAndNotify(this.getId());
        } catch (NotFoundException | IllegalThingException e) {
            e.printStackTrace();
        }
    }

    @Service(name = CService.CLOSE, description = "关闭电视")
    public void close() {
        String res = this.client.send("state", "close", "PUT");
        if(res == null) return;
        this.state = CState.OFF;
        try {
            Catcher.getThingMonitor().updateStateAndNotify(this.getId());
        } catch (NotFoundException | IllegalThingException e) {
            e.printStackTrace();
        }
    }

    @Service(name = "increase_volume", description = "增加音量")
    public void increaseVolume() {
        if(this.volume >= 100) return;
        String v = this.client.send("volume", null, "GET");
        int vol = Integer.parseInt(v)+1;
        this.client.send("volume", String.valueOf(vol), "PUT");
        this.volume = vol;
        try {
            Catcher.getThingMonitor().updatePropertyAndNotify(this.getId(), "volume");
        } catch (NotFoundException | IllegalThingException e) {
            e.printStackTrace();
        }
    }

    @Service(name = "decrease_volume", description = "减少音量")
    public void decreaseVolume() {
        if(this.volume <= 0) return;
        String v = this.client.send("volume", null, "GET");
        int vol = Integer.parseInt(v)-1;
        this.client.send("volume", String.valueOf(vol), "PUT");
        this.volume = vol;
        try {
            Catcher.getThingMonitor().updatePropertyAndNotify(this.getId(), "volume");
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
        } catch (NotFoundException | IllegalThingException e) {
            e.printStackTrace();
        }
    }

    public Tv(String type, String name, String description, String ip, String port) {
        super(type, name, description);
        this.client = new Client(ip, port);
    }
}
