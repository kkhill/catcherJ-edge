package com.kkhill;

import com.kkhill.common.config.PluginConfig;
import com.kkhill.core.Catcher;
import com.kkhill.core.exception.IllegalThingException;
import com.kkhill.core.plugin.PluginRegistry;
import io.leego.banana.BananaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;
import java.util.Properties;

public class Bootstrap {

    private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    public static void main(String[] args) {

        try {
            // read configuration
            Properties properties = new Properties();
            BufferedReader reader = new BufferedReader(new FileReader(PluginConfig.catcherConfigPath()));
            properties.load(reader);

            Catcher.initialize(Integer.parseInt((String) properties.get("scheduler.thread_num")),
                    Integer.parseInt((String) properties.get("scheduler.heartbeat")));

            // read addons and drivers
            FileInputStream addonsIn = new FileInputStream(new File(PluginConfig.addonConfigPath()));
            Map<String, Object> addons = new Yaml().load(addonsIn);
            addonsIn.close();

            FileInputStream driversIn = new FileInputStream(new File(PluginConfig.addonConfigPath()));
            Map<String, Object> drivers = new Yaml().load(driversIn);
            driversIn.close();

            // load and start
            Catcher.load(addons, drivers);
            Catcher.start();

            // registry catcher as a thing for management
            Catcher.getThingMonitor().registerThing(Catcher.getInstance());

        } catch (IOException | IllegalThingException e) {
            e.printStackTrace();
        }

        logger.info("bootstrap finished");
        logger.info(BananaUtils.bananaify("CATCHER"));
    }
}
