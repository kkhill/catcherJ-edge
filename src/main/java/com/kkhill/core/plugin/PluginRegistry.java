package com.kkhill.core.plugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PluginRegistry {

    private Map<String, Driver> driverRegistry;

    private Map<String, Addon> addonRegistry;

    private PluginRegistry() {
        this.driverRegistry = new ConcurrentHashMap<>();
        this.addonRegistry = new ConcurrentHashMap<>();
    }

    private static class Holder {
        private static PluginRegistry instance = new PluginRegistry();
    }

    public static PluginRegistry getInstance() {
        return Holder.instance;
    }

    public boolean registerPlugin(String name) {




        return true;
    }
}
