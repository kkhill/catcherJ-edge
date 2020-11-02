package com.kkhill;

import com.kkhill.common.config.PluginConfig;
import com.kkhill.core.Catcher;
import com.kkhill.core.exception.IllegalPluginConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;

public class Bootstrap {

    private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    public static void main(String[] args) {

        try {
            start();
        } catch (FileNotFoundException  e) {
            e.printStackTrace();
        }
    }

    public static void start() throws FileNotFoundException {
        // read configuration
        int threadNum = 5;
        int heartbeat = 1;
        // initialize system resources
        Catcher.initialize(threadNum, heartbeat);
        // register addons and drivers
        registryAddons();
        registryDrivers();
        // run catcher
        Catcher.start();
    }

    private static void registryAddons() throws FileNotFoundException {

        LinkedHashMap<String, Map<String, Object>> addons = readAddonConfig();
        for(String name : addons.keySet()) {
            Map<String, Object> addon = addons.get(name);
            if(!addon.containsKey(PluginConfig.PKG) || !addon.containsKey(PluginConfig.ENTRY)) {
                logger.error(String.format("register addon error: %s. configuration must has 'pkg' and 'entry' ", name));
                continue;
            }
            try{
                Catcher.getPluginRegistry().registerAddon(name, (String)addon.get(PluginConfig.PKG),
                        (String)addon.get(PluginConfig.ENTRY), addon.get(PluginConfig.CONFIG));
                logger.info(String.format("register addon: %s", name));
            } catch (IllegalPluginConfig e) {
                logger.error(String.format("register addon error: %s", name));
                e.printStackTrace();
            }

        }

    }

    private static void registryDrivers() throws FileNotFoundException {

        LinkedHashMap<String, Map<String, Object>> drivers = readDriverConfig();
        for(String name : drivers.keySet()) {
            Map<String, Object> addon = drivers.get(name);
            if(!addon.containsKey(PluginConfig.PKG) || !addon.containsKey(PluginConfig.ENTRY)) {
                logger.error(String.format("register driver error: %s. configuration must has 'pkg' and 'entry' ", name));
                continue;
            }
            try{
                Catcher.getPluginRegistry().registerDriver(name, (String)addon.get(PluginConfig.PKG),
                        (String)addon.get(PluginConfig.ENTRY), addon.get(PluginConfig.CONFIG));
                logger.info(String.format("register driver: %s", name));
            } catch (IllegalPluginConfig e) {
                logger.error(String.format("register driver error: %s", name));
                e.printStackTrace();
            }
        }
    }

    private static LinkedHashMap<String, Map<String, Object>> readAddonConfig() throws FileNotFoundException {
        LinkedHashMap<String, Map<String, Object>> res = readPluginConfig(PluginConfig.getAddonConfigPath());
        return res == null ? new LinkedHashMap<>() : res;
    }

    private static LinkedHashMap<String, Map<String, Object>> readDriverConfig() throws FileNotFoundException {
        LinkedHashMap<String, Map<String, Object>> res = readPluginConfig(PluginConfig.getDriverConfigPath());
        return res == null ? new LinkedHashMap<>() : res;
    }

    private static LinkedHashMap<String, Map<String, Object>> readPluginConfig(String file) throws FileNotFoundException {
        return new Yaml().load(new FileInputStream(new File(file)));
    }
}
