package com.kkhill.core.plugin;

import com.kkhill.core.Catcher;
import com.kkhill.core.exception.IllegalPluginConfigException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PluginRegistry {

    private static String addonPkgPath = "com.kkhill.addons";
    private static String driverPkgPath = "com.kkhill.drivers";

    private final Map<String, Driver> drivers;
    private Map<String, Addon> addons;

    private PluginRegistry() {
        this.drivers = new ConcurrentHashMap<>();
        this.addons = new ConcurrentHashMap<>();
    }

    private static class Holder {
        private static PluginRegistry instance = new PluginRegistry();
    }

    public static PluginRegistry getInstance() { return Holder.instance; }

    public void registerDriver(String entry, Object config) {
        try {
            Driver p = (Driver)loadPlugin(String.format("%s.%s", driverPkgPath, entry));
            p.load(config);
            this.drivers.put(entry, p);
        } catch (IllegalPluginConfigException e) {
            e.printStackTrace();
        }
    }

    public void registerAddon(String entry, Object config) throws IllegalPluginConfigException {
        // register addons synchronously for safety
        Addon p = (Addon)loadPlugin(String.format("%s.%s", addonPkgPath, entry));
        p.load(config);
        this.addons.put(entry, p);
    }

    public void startAddon(String entry) {
        this.addons.get(entry).start();
    }

    public void stopAddon(String entry) {
        this.addons.get(entry).stop();
    }

    public void startAllAddons() {
        for(String entry : this.addons.keySet()) {
            this.addons.get(entry).start();
        }
    }

    public void stopAllAddons() {
        for(String entry : this.addons.keySet()) {
            this.addons.get(entry).stop();
        }
    }

    public void unloadAllPlugins() {
        unloadAllAddons();
        unloadAllDrivers();
    }

    public void unloadAllAddons() {
        for(String entry : this.addons.keySet()) {
            this.addons.get(entry).unload();
        }
    }

    public void unloadAllDrivers() {
        for(String entry : this.drivers.keySet()) {
            this.drivers.get(entry).unload();
        }
    }

    public void unloadPlugin(String entry) {
        if(this.addons.containsKey(entry)) this.addons.get(entry).unload();
        if(this.drivers.containsKey(entry)) this.drivers.get(entry).unload();
    }

    private Plugin loadPlugin(String name) throws IllegalPluginConfigException {

        try {
            return (Plugin) Class.forName(name).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalPluginConfigException(String.format("can not find addon class: %s ", name));
        }
    }
}
