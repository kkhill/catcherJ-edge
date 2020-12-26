package com.kkhill.common.config;

public class PluginConfig {

    public static final String PKG = "pkg";
    public static final String ENTRY= "entry";
    public static final String CONFIG = "config";


    private static String addonConfigPath = "src/main/resources/addons.yaml";
    private static String driverConfigPath = "src/main/resources/drivers.yaml";

    public static void setAddonConfigPath(String addonConfigPath) {
        PluginConfig.addonConfigPath = addonConfigPath;
    }

    public static void setDriverConfigPath(String driverConfigPath) {
        PluginConfig.driverConfigPath = driverConfigPath;
    }

    public static String getAddonConfigPath() {
        return addonConfigPath;
    }

    public static String getDriverConfigPath() {
        return driverConfigPath;
    }
}
