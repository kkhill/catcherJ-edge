package com.kkhill.core;

import com.kkhill.Bootstrap;
import com.kkhill.core.exception.IllegalPluginConfig;
import com.kkhill.core.exception.NotFoundException;
import com.kkhill.core.exception.IllegalThingException;
import com.kkhill.driver.demo.thing.Light;
import org.junit.Test;

import java.io.FileNotFoundException;

public class TestCore {

    @Test
    public void testBootstrap() {
        try {
            Bootstrap.start();
        } catch (FileNotFoundException | IllegalPluginConfig e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testDemoDriver() {
        Light light = new Light("lovely", true, "127", "8000");
        try {
            Catcher.getThingMonitor().registerThing(light);
            light.open();
            Catcher.getThingMonitor().callService(light.getID(), "set_brightness_and_temperature", new Object[]{50, 40});
        } catch (IllegalThingException | NotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void testSingleDriver() {
        Light light = new Light("lovely", true, "127", "8000");
        try {
            Catcher.getThingMonitor().registerThing(light);
            light.open();
            Catcher.getThingMonitor().callService(light.getID(), "set_brightness_and_temperature", new Object[]{50, 40});
        } catch (IllegalThingException | NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPluginRegistry() {

    }

    @Test
    public void testEventBus() {

    }
}
