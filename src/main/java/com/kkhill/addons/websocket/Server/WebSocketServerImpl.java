package com.kkhill.addons.websocket.Server;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class WebSocketServerImpl extends WebSocketServer {

    private final Logger logger = LoggerFactory.getLogger(WebSocketServerImpl.class);

    public WebSocketServerImpl(String host, int port) {
        super(new InetSocketAddress(host, port));
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        logger.debug("web socket connection opened: {}", clientHandshake.getResourceDescriptor());
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        logger.debug("web socket connection closed: {}", s);
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        logger.debug("message received: {}", s);
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        logger.debug("websocket connection error");
        e.printStackTrace();
        if (webSocket != null) {
            // some errors like port binding failed may not be assignable to a specific websocket
        }
    }

    @Override
    public void onStart() {
        logger.debug("websocket server on start");
        setConnectionLostTimeout(60);
    }
}
