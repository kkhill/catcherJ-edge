package com.kkhill.core;

import com.kkhill.core.event.EventBus;
import com.kkhill.core.plugin.PluginRegistry;
import com.kkhill.core.scheduler.Scheduler;
import com.kkhill.core.thing.ThingMonitor;

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
}
