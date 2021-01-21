package com.kkhill.addons.storage;

import com.kkhill.core.Catcher;
import com.kkhill.core.exception.NotFoundException;
import com.kkhill.core.plugin.Addon;

import java.util.HashMap;
import java.util.Map;

public class Storage implements Addon {

    @Override
    public boolean load(Object data) {

        try {
            String[] packages = new String[]{"com.kkhill.addons.storage.resources"};
            Map<String, Object> args = new HashMap<>();
            args.put("packages", packages);
            Catcher.getThingMonitor().callService("addons.http.server", "addResources", args);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean unload() {
        return false;
    }

    @Override
    public boolean start() {
        return false;
    }

    @Override
    public boolean stop() {
        return false;
    }
}
