package com.kkhill.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class PluginConfig {

    private static final Logger logger = LoggerFactory.getLogger(PluginConfig.class);

    private static String configDir = System.getProperty("user.home") + File.separator + ".catcher" + File.separator;
    private static String catcherConfigFile = "catcher.properties";
    private static String logConfigFile = "log4j.properties";
    private static String ruleConfigFile = "rules.yaml";
    private static String addonConfigFile = "addons.yaml";
    private static String driverConfigFile = "drivers.yaml";

    static {
        try {
            checkDirOrCreate(new File(configDir()));

            if(!checkFileOrCreate(new File(catcherConfigPath()))) {
                FileOutputStream out = new FileOutputStream(new File(catcherConfigPath()));
                Properties prop = new Properties();
                prop.setProperty("scheduler.heartbeat", String.valueOf(5));
                prop.setProperty("scheduler.thread_num", String.valueOf(50));
                prop.store(out, "default catcher config");
                out.close();
            }
            if(!checkFileOrCreate(new File(addonConfigPath()))) {
                Map<String, Object> addons = new LinkedHashMap<>();
                Map<String, Object> http = new HashMap<>();
                http.put("host", "127.0.0.1");
                http.put("port", 8000);
                addons.put("http.Http", http);
                Map<String, Object> websocket = new HashMap<>();
                websocket.put("host", "127.0.0.1");
                websocket.put("port", 8001);
                addons.put("websocket.WebSocket", websocket);
                addons.put("automation.Automation", null);
                FileWriter out = new FileWriter(new File(addonConfigPath()));
                out.write(new Yaml().dumpAsMap(addons));
                out.flush();
                out.close();
            }
            checkFileOrCreate(new File(logConfigPath()));
            checkFileOrCreate(new File(ruleConfigPath()));
            checkFileOrCreate(new File(driverConfigPath()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String configDir() {
        return configDir;
    }

    public static String catcherConfigPath() {
        return configDir + catcherConfigFile;
    }

    public static String logConfigPath() {
        return configDir + logConfigFile;
    }

    public static String ruleConfigPath() {
        return configDir + ruleConfigFile;
    }

    public static String addonConfigPath() {
        return configDir + addonConfigFile;
    }

    public static String driverConfigPath() {
        return configDir + driverConfigFile;
    }

    public static boolean checkFileOrCreate(File file) {
        if(!file.exists() || !file.isFile()) {
            try {
                file.createNewFile();
                logger.info("no {}, created", file.toString());
                return false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
    public static boolean checkDirOrCreate(File file) {
        if(!file.exists() || !file.isDirectory()) {
            file.mkdir();
            logger.info("no {}, created", file.toString());
            return false;
        }
        return true;
    }
}
