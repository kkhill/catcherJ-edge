package com.kkhill;

import com.kkhill.common.config.PluginConfig;
import com.kkhill.core.Catcher;
import com.kkhill.core.exception.IllegalThingException;
import io.leego.banana.BananaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;

public class Bootstrap {

    private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    public static void main(String[] args) {
        // read configuration
        int threadNum = 5;
        int heartbeat = 1;

        Catcher.initialize(threadNum, heartbeat);
        try {
            LinkedHashMap<String, Object> addons = readAddonConfig();
            LinkedHashMap<String, Object> drivers = readDriverConfig();
            Catcher.load(addons, drivers);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Catcher.start();

        // registry catcher as a thing to manage
        try {
            Catcher.getThingMonitor().registerThing(Catcher.getInstance());
        } catch (IllegalThingException e) {
            e.printStackTrace();
        }
        logger.info("bootstrap finished");
        logger.info(BananaUtils.bananaify("CATCHER"));
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
