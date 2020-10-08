package com.kkhill.driver.demo.lib;

public class Client {

    private boolean state;
    private int brightness;
    private int temperature;

    public boolean open() {
        System.out.println("open the light with client");
        if(!state) state = true;
        return true;
    }

    public boolean close() {
        System.out.println("close the light with client");
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
