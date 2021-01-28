package com.kkhill.addons.websocket.Server;

import com.kkhill.common.thing.CState;
import com.kkhill.core.annotation.State;
import com.kkhill.core.thing.AddonThing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class CatcherWebSocketServer extends AddonThing {

    private final Logger logger = LoggerFactory.getLogger(CatcherWebSocketServer.class);

    private String host;
    private int port;
    private WebSocketServerImpl server;

    @State
    public String state = CState.ON;

    public CatcherWebSocketServer(String host, int port) {
        super("server", "a websocket server");
        this.host = host;
        this.port = port;
    }

    public void start() throws IOException, InterruptedException {
        server = new WebSocketServerImpl(host, port);
        server.start();
    }

    public void stop() throws IOException, InterruptedException {
        server.stop();
    }

    public void broadcast(byte[] data) {
        server.broadcast(data);
    }

    public void broadcast(String data) {
        server.broadcast(data);
    }

    public static void main(String[] args) {
        try {
            new CatcherWebSocketServer("localhost", 8001).start();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
