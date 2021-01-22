package com.kkhill.drivers.demolight1.lib;

public class Client {

    private String ip;
    private String port;
    private boolean state;
    private int brightness = 70;
    private int temperature;

    public Client(String ip, String port) {
        this.ip = ip;
        this.port = port;
    }

    public boolean open() {
        if(!state) state = true;
        return true;
    }

    public boolean close() {
        if(state) state = false;
        return true;
    }

    public boolean state() {
        return state;
    }

    public int getBrightness() {
        return brightness;
    }

    public boolean setBrightness(int brightness) {
        this.brightness = brightness;
        return true;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getTemperature() {
        return temperature;
    }

    public boolean setTemperature(int temperature) {
        this.temperature = temperature;
        return true;
    }
}
