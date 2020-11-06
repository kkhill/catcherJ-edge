package com.kkhill.addons.api;

import com.kkhill.core.plugin.Addon;

public class ApiAddon implements Addon {
    @Override
    public boolean load(Object data) {
        System.out.println("I want to expose my api");
        return false;
    }

    @Override
    public boolean unload(Object data) {
        return false;
    }
}
