package com.kkhill.core;

import com.kkhill.core.event.EventBus;
import com.kkhill.core.plugin.PluginRegistry;
import com.kkhill.core.scheduler.Scheduler;
import com.kkhill.core.thing.ThingMonitor;
import com.kkhill.core.exception.IllegalThingException;
import com.kkhill.core.exception.ServiceNotFoundException;
import com.kkhill.core.exception.ThingNotFoundException;
import com.kkhill.driver.demo.lib.Client;
import com.kkhill.driver.demo.thing.Light;

public class Catcher {

    private Catcher() {}

    private static class Holder {
        private static EventBus eventBus = EventBus.getInstance();
        private static ThingMonitor monitor = ThingMonitor.getInstance();
        private static PluginRegistry pluginRegistry = PluginRegistry.getInstance();
        private static Scheduler scheduler = Scheduler.getInstance();
    }

    public static ThingMonitor getThingMonitor() {
        return Holder.monitor;
    }

    public static EventBus getEventBus() {
        return Holder.eventBus;
    }

    public static PluginRegistry getPluginRegistry() {
        return Holder.pluginRegistry;
    }

    public static Scheduler getScheduler() {
        return Holder.scheduler;
    }

    public static void initialize(int poolSize, int pollingInternal, int heartbeat) {
        Scheduler.initialize(poolSize, pollingInternal, heartbeat);
    }

    /**
     * start up catcher
     */
    public static void start() {


        // load addons
        testSingleDriver();
        // load drivers

        // run scheduler to keep program going
        getScheduler().start();
    }

    public static void testSingleDriver() {
        Light light = new Light("lovely", true, new Client());
        try {
            Catcher.getThingMonitor().registerThing(light);
            light.open();
            Catcher.getThingMonitor().callService(light.getID(), "set_brightness_and_temperature", new Object[]{50, 40});
        } catch (IllegalThingException | ThingNotFoundException | ServiceNotFoundException e) {
            e.printStackTrace();
        }
    }
}
