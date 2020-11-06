package com.kkhill.core.plugin;

import com.kkhill.core.exception.IllegalPluginConfig;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PluginRegistry {

    private static String addonPkgPath = "com.kkhill.addons";
    private static String driverPkgPath = "com.kkhill.drivers";

    private Map<String, Driver> drivers;
    private Map<String, Addon> addons;

    private PluginRegistry() {
        this.drivers = new ConcurrentHashMap<>();
        this.addons = new ConcurrentHashMap<>();
    }

    private static class Holder {
        private static PluginRegistry instance = new PluginRegistry();
    }

    public static PluginRegistry getInstance() {
        return Holder.instance;
    }

    public void registerDriver(String name, String pkg, String driver, Object config) throws IllegalPluginConfig {

        try {
            Driver p = (Driver)loadPlugin(String.format("%s.%s.%s", driverPkgPath, pkg, driver));
            p.load(config);
            this.drivers.put(name, p);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | ClassCastException e) {
            throw new IllegalPluginConfig(String.format("can not find driver class: %s", driver));
        }
    }

    public void registerAddon(String name, String pkg, String addon, Object config) throws IllegalPluginConfig {
        try {
            Addon p = (Addon)loadPlugin(String.format("%s.%s.%s", addonPkgPath, pkg, addon));
            p.load(config);
            this.addons.put(name, p);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | ClassCastException e) {
            throw new IllegalPluginConfig(String.format("can not find addon class: %s ", addon));
        }
    }

    public Plugin loadPlugin(String name)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> c = Class.forName(name);
        return (Plugin) c.newInstance();
    }
}
