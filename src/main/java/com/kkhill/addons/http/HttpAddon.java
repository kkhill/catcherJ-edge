package com.kkhill.addons.http;

import com.kkhill.addons.http.server.CatcherHttpServer;
import com.kkhill.core.Catcher;
import com.kkhill.core.exception.IllegalThingException;
import com.kkhill.core.plugin.Addon;


public class HttpAddon implements Addon {

    private CatcherHttpServer server;

    @Override
    public boolean load(Object data) {

        try {
            server = new CatcherHttpServer();
            server.resourceConfig.packages("com.kkhill.addons.http.resources");
            server.start();
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
