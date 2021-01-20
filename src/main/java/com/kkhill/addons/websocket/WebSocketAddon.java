package com.kkhill.addons.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkhill.addons.websocket.Server.CatcherWebSocketServer;
import com.kkhill.common.event.EventType;
import com.kkhill.core.Catcher;
import com.kkhill.core.event.Event;
import com.kkhill.core.event.EventConsumer;
import com.kkhill.core.exception.IllegalThingException;
import com.kkhill.core.plugin.Addon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class WebSocketAddon implements Addon, EventConsumer {

    private final Logger logger = LoggerFactory.getLogger(WebSocketAddon.class);

    private CatcherWebSocketServer server;

    @Override
    @SuppressWarnings("unchecked")
    public boolean load(Object data) {

        LinkedHashMap<String, Object> config = (LinkedHashMap<String, Object>)data;
        try {
            server = new CatcherWebSocketServer((String)config.get("host"), (int)config.get("port"));
            server.start();
            Catcher.getThingMonitor().registerThing(server);
        } catch (IOException | InterruptedException e) {
            logger.error("failed to start websocket server");
            e.printStackTrace();
        } catch (IllegalThingException e) {
            e.printStackTrace();
        }

        // listen state updated and property updated events to notify websocket client
        Catcher.getEventBus().listen(EventType.STATE_UPDATED, this);
        Catcher.getEventBus().listen(EventType.PROPERTY_UPDATED, this);

        return true;
    }

    @Override
    public boolean unload(Object data) {
        try {
            server.stop();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void handle(Event event) {
        try {
            byte[] data = new ObjectMapper().writeValueAsBytes(event.getData());
            server.broadcast(data);
            System.out.println(event.getData());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
