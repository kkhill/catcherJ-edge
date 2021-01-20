package com.kkhill.addons.websocket.Server;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class WebSocketServerImpl extends WebSocketServer {

    public WebSocketServerImpl(String host, int port) {
        super(new InetSocketAddress(host, port));
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        webSocket.send("hand shake success");
        broadcast("new connection: " + clientHandshake
                .getResourceDescriptor()); //This method sends a message to all clients connected
        System.out.println(
                webSocket.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!");
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        broadcast(webSocket + " has left the room!");
        System.out.println(webSocket + " has left the room!");
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        broadcast("ok");
        System.out.println(webSocket + ": " + s);
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        e.printStackTrace();
        if (webSocket != null) {
            // some errors like port binding failed may not be assignable to a specific websocket
        }
    }

    @Override
    public void onStart() {
        System.out.println("Server started!");
        setConnectionLostTimeout(100);
    }
}
