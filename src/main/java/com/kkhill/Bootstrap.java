package com.kkhill;

import com.kkhill.utils.config.PluginConfig;
import com.kkhill.core.Catcher;
import com.kkhill.core.exception.IllegalPluginConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;

public class Bootstrap {

    private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    public static void test() {
    }

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

        // test code
        test();
    }

    private static void registryAddons() throws FileNotFoundException {

        LinkedHashMap<String, Object> addons = readAddonConfig();
        for(String entry : addons.keySet()) {
            Object config = addons.get(entry);
            try{
                Catcher.getPluginRegistry().registerAddon(entry, config);
                logger.info(String.format("register addon: %s", entry));
            } catch (IllegalPluginConfig e) {
                logger.error(String.format("register addon error: %s", entry));
                e.printStackTrace();
            }

        }

    }

    private static void registryDrivers() throws FileNotFoundException {

        LinkedHashMap<String, Object> drivers = readDriverConfig();
        for(String entry : drivers.keySet()) {
            Object config = drivers.get(entry);
            try{
                Catcher.getPluginRegistry().registerDriver(entry, config);
                logger.info(String.format("register driver: %s", entry));
            } catch (IllegalPluginConfig e) {
                logger.error(String.format("register driver error: %s", entry));
                e.printStackTrace();
            }
        }
    }

    private static LinkedHashMap<String, Object> readAddonConfig() throws FileNotFoundException {
        LinkedHashMap<String, Object> res = readPluginConfig(PluginConfig.getAddonConfigPath());
        return res == null ? new LinkedHashMap<>() : res;
    }

    private static LinkedHashMap<String, Object> readDriverConfig() throws FileNotFoundException {
        LinkedHashMap<String, Object> res = readPluginConfig(PluginConfig.getDriverConfigPath());
        return res == null ? new LinkedHashMap<>() : res;
    }

    private static LinkedHashMap<String, Object> readPluginConfig(String file) throws FileNotFoundException {
        return new Yaml().load(new FileInputStream(new File(file)));
    }
}
