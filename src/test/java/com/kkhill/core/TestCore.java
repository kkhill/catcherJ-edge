package com.kkhill.core;

import com.kkhill.core.exception.IllegalThingException;
import com.kkhill.core.exception.ServiceNotFoundException;
import com.kkhill.core.exception.ThingNotFoundException;
import com.kkhill.core.thing.Thing;
import com.kkhill.driver.demo.lib.Client;
import com.kkhill.driver.demo.thing.Light;
import org.junit.Test;

public class TestCore {

    @Test
    public void testRegisterThing() {
        Light light = new Light("lovely", true, new Client());
        try {
            Catcher.getThingMonitor().registerThing(light);
            light.open();
            Catcher.getThingMonitor().callService(light.getID(), "set_brightness_and_temperature", new Object[]{50, 40});
        } catch (IllegalThingException | ThingNotFoundException | ServiceNotFoundException illegalThingException) {
            illegalThingException.printStackTrace();
        }


    }
}
