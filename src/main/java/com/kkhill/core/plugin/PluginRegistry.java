package com.kkhill.core.plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    public boolean loadPlugin(String plugin) {

        try {
            Class a = Class.forName("com.kkhill.driver.demo." + plugin);
            System.out.println(a.getCanonicalName());
            Object p = a.newInstance();
            Method m = a.getDeclaredMethod("initialize");
            m.invoke(p);

        } catch (ClassNotFoundException | InstantiationException | NoSuchMethodException
                | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }


        return true;
    }

    public boolean registerPlugin(String name) {

        return true;
    }
}
