package com.kkhill.addons.http;

import com.kkhill.addons.http.server.CatcherHttpServer;
import com.kkhill.core.Catcher;
import com.kkhill.core.exception.IllegalThingException;
import com.kkhill.core.plugin.Addon;

import java.util.LinkedHashMap;


public class HttpAddon implements Addon {
    /**
     * provide http server and basic API(thing)
     */

    private CatcherHttpServer server;

    @Override
    @SuppressWarnings("unchecked")
    public boolean load(Object data) {
        LinkedHashMap<String, Object> config = (LinkedHashMap<String, Object>)data;
        server = new CatcherHttpServer((String)config.get("host"), (int)config.get("port"));
        server.resourceConfig.packages("com.kkhill.addons.http.resources");
        server.start();

        try {
            Catcher.getThingMonitor().registerThing(server);
        } catch (IllegalThingException e) {
            e.printStackTrace();
        }

        System.out.println("http server had been started");
        return false;
    }

    @Override
    public boolean unload(Object data) {
        server.stop();
        return false;
    }
}
