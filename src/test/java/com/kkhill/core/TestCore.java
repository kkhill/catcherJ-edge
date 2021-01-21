package com.kkhill.core;

import com.kkhill.Bootstrap;
import com.kkhill.core.exception.NotFoundException;
import com.kkhill.core.exception.IllegalThingException;
import com.kkhill.drivers.demolight1.thing.Light;
import com.kkhill.common.thing.CommonThing;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class TestCore {

    @Test
    public void testDemoDriver() {
        Light light = new Light(CommonThing.LIGHT, "lovely", "a test light", "127", "8000");
        try {
            Catcher.getThingMonitor().registerThing(light);
            light.open();
            Map<String, Object> args = new HashMap<>();
            args.put("wrong", 20);
            args.put("temperature", 30);
            args.put("brightness", 50);

            Catcher.getThingMonitor().callServiceAndNotify(light.getId(), "set_brightness_and_temperature", args);
        } catch (IllegalThingException | NotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void testSingleDriver() {
        Light light = new Light(CommonThing.LIGHT, "lovely", "a test light", "127", "8000");
        try {
            Catcher.getThingMonitor().registerThing(light);
            light.open();
            Map<String, Object> args = new HashMap<>();
            args.put("brightness", 50);
            args.put("temperature", 30);
            Catcher.getThingMonitor().callServiceAndNotify(light.getId(), "set_brightness_and_temperature", args);
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
