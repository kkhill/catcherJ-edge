package com.kkhill.addons.http;

import com.kkhill.addons.http.server.CatcherHttpServer;
import com.kkhill.core.Catcher;
import com.kkhill.core.exception.IllegalThingException;
import com.kkhill.core.plugin.Addon;
import com.kkhill.core.scheduler.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;


public class Http implements Addon {
    /**
     * provide http server and basic API(thing)
     */

    private final Logger logger = LoggerFactory.getLogger(Http.class);

    private CatcherHttpServer server;

    @Override
    @SuppressWarnings("unchecked")
    public boolean load(Object data) {
        LinkedHashMap<String, Object> config = (LinkedHashMap<String, Object>)data;
        server = new CatcherHttpServer((String)config.get("host"), (int)config.get("port"));
        server.resourceConfig.packages("com.kkhill.addons.http.resources");

        try {
            Catcher.getThingMonitor().registerThing(server);
        } catch (IllegalThingException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean unload() {
        return true;
    }

    @Override
    public boolean start() {
        server.start();
        logger.info("http server started");
        return true;
    }

    @Override
    public boolean stop() {
        server.stop();
        return true;
    }
}
