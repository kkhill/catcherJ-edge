package com.kkhill.core;

import com.kkhill.common.thing.CState;
import com.kkhill.core.annotation.Service;
import com.kkhill.core.annotation.ServiceParam;
import com.kkhill.core.annotation.State;
import com.kkhill.core.event.EventBus;
import com.kkhill.core.exception.IllegalPluginConfigException;
import com.kkhill.core.plugin.PluginRegistry;
import com.kkhill.core.scheduler.Scheduler;
import com.kkhill.core.thing.SystemThing;
import com.kkhill.core.thing.ThingMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Catcher extends SystemThing {

    private static final Logger logger = LoggerFactory.getLogger(Catcher.class);

    private static class Holder {
        private static final EventBus eventBus = EventBus.getInstance();
        private static final ThingMonitor monitor = ThingMonitor.getInstance();
        private static final PluginRegistry pluginRegistry = PluginRegistry.getInstance();
        private static final Scheduler scheduler = Scheduler.getInstance();
    }

    public static ThingMonitor getThingMonitor() { return Holder.monitor; }

    public static EventBus getEventBus() { return Holder.eventBus; }

    public static PluginRegistry getPluginRegistry() { return Holder.pluginRegistry; }

    public static Scheduler getScheduler() { return Holder.scheduler; }

    public static void initialize(int poolSize, int heartbeat) {
        Scheduler.initialize(poolSize, heartbeat);
    }

    public static void load(Map<String, Object> addons, Map<String, Object> drivers) {

        state = CState.LOADING;
        // registry addons
        for(String entry : addons.keySet()) {
            registerAddon(entry, addons.get(entry));
        }
        // registry driver
        for(String entry : drivers.keySet()) {
            registerDriver(entry, drivers.get(entry));
        }

        logger.info("plugins loaded: {} addons and {} drivers", addons.size(), drivers.size());
    }

    public static void start() {

        state = CState.STARTING;
        // start up addons
        getPluginRegistry().startAllAddons();
        // run scheduler to keep program going
        getScheduler().start();
        state = CState.RUNNING;
    }

    public static void stop() {

        state = CState.STOPPING;
        // stop addons
        getPluginRegistry().stopAllAddons();
        // stop scheduler
        getScheduler().stop();
    }

    public static void unload() {

        state = CState.UNLOADING;
        getPluginRegistry().unloadAllPlugins();
        state = CState.OFFLINE;
    }

    public static void registerAddon(String entry, Object config) {

        try {
            Catcher.getPluginRegistry().registerAddon(entry, config);
            logger.info(String.format("addon registered: %s", entry));
        } catch (IllegalPluginConfigException e) {
            e.printStackTrace();
            logger.error(String.format("failed to register addon: %s", entry));
        }
    }

    public static void registerDriver(String entry, Object config) {

        try {
            Catcher.getPluginRegistry().registerDriver(entry, config);
        } catch (IllegalPluginConfigException e) {
            e.printStackTrace();
            logger.error("failed to register driver: {}", entry);
        }
    }


    /** definition of catcher thing, which need to be used as a thing instance **/

    private Catcher() {
        super("catcher", "believe me", true);
    }

    private static class Instance {
       private final static Catcher catcher = new Catcher();
    }

    public static Catcher getInstance() { return Instance.catcher; };

    @State
    public static String state = CState.OFFLINE;

    @Service(name="register_addon", description="register a addon")
    public void registerAddonS(@ServiceParam(name="entry", description="package.plugin in addon directory") String entry,
                                @ServiceParam(name="config", description="addon configuration to load") Object config) {

        registerAddon(entry, config);
    }

    @Service(name="register_driver", description="register a driver")
    public void registerDriverS(@ServiceParam(name="entry", description="package.plugin in driver directory") String entry,
                                     @ServiceParam(name="config", description="driver configuration to load") Object config) {

        registerDriver(entry, config);
    }
}