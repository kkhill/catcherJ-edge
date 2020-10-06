package com.kkhill.driver.demo;

import com.kkhill.core.thing.Thing;

public class Light extends Thing {


    public String state;

    public int brightness;

    private int temperature;

    public void open() {
        System.out.println("open the light");
    }

    public void close() {
        System.out.println("close the light");
    }

    public Light(String friendlyName, boolean available) {
        super(friendlyName, available);
    }

}
