package com.kkhill.core.plugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PluginRegistry {

    private Map<String, Plugin> registry;

    private PluginRegistry() {
        this.registry = new ConcurrentHashMap<>();
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
