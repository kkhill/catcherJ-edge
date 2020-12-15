package com.kkhill.addons.http;

import com.kkhill.addons.http.handler.TestHandler;
import com.kkhill.addons.http.handler.ThingsHandler;
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
            Catcher.getThingMonitor().registerThing(server);
            registryHttpApi();
        } catch (IllegalThingException | IOException | NotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("http server had been started");
        return false;
    }

    @Override
    public boolean unload(Object data) {
        return false;
    }

    public void registryHttpApi() throws NotFoundException {
        Catcher.getThingMonitor().callServiceAndNotify("httpserver", "route",
                "/test", new TestHandler());
        Catcher.getThingMonitor().callServiceAndNotify("httpserver", "route",
                "/things", new ThingsHandler());
    }
}
