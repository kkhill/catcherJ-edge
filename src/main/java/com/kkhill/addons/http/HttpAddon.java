package com.kkhill.addons.http;

import com.kkhill.addons.http.server.CatcherHttpServer;
import com.kkhill.core.Catcher;
import com.kkhill.core.exception.IllegalThingException;
import com.kkhill.core.exception.NotFoundException;
import com.kkhill.core.plugin.Addon;

import java.io.IOException;

public class HttpAddon implements Addon {
    @Override
    public boolean load(Object data) {

        try {
            CatcherHttpServer server = new CatcherHttpServer();
            server.addResource("com.kkhill.addons.http.resources");
            server.start();
            Catcher.getThingMonitor().registerThing(server);
        } catch (IllegalThingException | IOException e) {
            e.printStackTrace();
        }

        System.out.println("http server had been started");
        return false;
    }

    @Override
    public boolean unload(Object data) {
        return false;
    }
}
