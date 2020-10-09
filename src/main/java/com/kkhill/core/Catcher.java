package com.kkhill.core;

import com.kkhill.core.event.EventBus;
import com.kkhill.core.plugin.PluginRegistry;
import com.kkhill.core.thing.ThingMonitor;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Catcher {

    private Catcher() {}

    private static class Holder {
        private static EventBus eventBus = EventBus.getInstance();
        private static ThingMonitor monitor = ThingMonitor.getInstance();
        private static PluginRegistry pluginRegistry = PluginRegistry.getInstance();
        private static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10);
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

    public static ScheduledThreadPoolExecutor getExecutor() {
        return Holder.executor;
    }

    /**
     * start up catcher
     */
    public void start() {

        // load addons

        // load drivers

        // set up all poll method

        //
    }
}
