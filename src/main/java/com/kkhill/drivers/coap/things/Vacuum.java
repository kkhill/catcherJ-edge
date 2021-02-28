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

public class Vacuum extends Thing {

    private Client client;

    @State(description = "state")
    public String state = CState.OFFLINE;

    @Property(name = "vendor", description = "设备厂商")
    public String vendor = "zh2k3ang";

    @Property(name = CProperty.SPEED, description = "转速", unitOfMeasurement="int")
    public int speed;

    @Service(name = CService.START, description = "启动扫地机器人")
    public void start() {
        String res = this.client.send("state", "start", "PUT");
        if(res == null) return;
        this.state = CState.IDLE;
        try {
            Catcher.getThingMonitor().updateStateAndNotify(this.getId());
        } catch (NotFoundException | IllegalThingException e) {
            e.printStackTrace();
        }
    }

    @Service(name = CService.STOP, description = "关闭扫地机器人")
    public void stop() {
        String res = this.client.send("state", "stop", "PUT");
        if(res == null) return;
        this.state = CState.OFFLINE;
        try {
            Catcher.getThingMonitor().updateStateAndNotify(this.getId());
        } catch (NotFoundException | IllegalThingException e) {
            e.printStackTrace();
        }
    }

    @Service(name = CService.CLEAN, description = "开始清洁")
    public void clean() {
        String res = this.client.send("state", "clean", "PUT");
        if(res == null) return;
        this.state = CState.WORKING;
        try {
            Catcher.getThingMonitor().updateStateAndNotify(this.getId());
        } catch (NotFoundException | IllegalThingException e) {
            e.printStackTrace();
        }
    }

    @Service(name = CService.PAUSE, description = "暂停清洁")
    public void pause() {
        String res = this.client.send("state", "pause", "PUT");
        if(res == null) return;
        this.state = CState.IDLE;
        try {
            Catcher.getThingMonitor().updateStateAndNotify(this.getId());
        } catch (NotFoundException | IllegalThingException e) {
            e.printStackTrace();
        }
    }

    @Service(name = "update", description = "update data", poll = true)
    public void update() {
        try {
            String s = this.client.send("state", null, "GET");
            this.state = s == null ? CState.OFFLINE : s;
            Catcher.getThingMonitor().updateStateAndNotify(this.getId());

            String t = this.client.send("speed", null, "GET");
            if(t != null) {
                this.speed = Integer.parseInt(t);
                Catcher.getThingMonitor().updatePropertyAndNotify(this.getId(), "speed");
            }
        } catch (NotFoundException | IllegalThingException e) {
            e.printStackTrace();
        }
    }

    public Vacuum(String type, String name, String description, String ip, String port) {
        super(type, name, description);
        this.client = new Client(ip, port);
    }
}
