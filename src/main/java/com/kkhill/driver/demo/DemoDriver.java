package com.kkhill.driver.demo;

import com.kkhill.core.Catcher;

public class DemoDriver {


    public void initialize() {
        Catcher.getPluginRegistry().registerPlugin("demo");
        // add things to core system.

    }
}
