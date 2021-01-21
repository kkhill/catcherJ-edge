package com.kkhill.addons.websocket.Server;

import com.kkhill.common.thing.CommonState;
import com.kkhill.core.annotation.State;
import com.kkhill.core.thing.AddonThing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class CatcherWebSocketServer extends AddonThing {

    private final Logger logger = LoggerFactory.getLogger(CatcherWebSocketServer.class);

    private String host;
    private int port;
    private WebSocketServerImpl server;

    @State
    public String state = CommonState.ON;

    public CatcherWebSocketServer(String host, int port) {
        super("server", "a websocket server");
        this.host = host;
        this.port = port;
    }

    public void start() throws IOException, InterruptedException {
        server = new WebSocketServerImpl(host, port);
        server.start();
        logger.info("websocket server started");

        new ScheduledThreadPoolExecutor(3).schedule(()->{
            server.broadcast("hello, my name is catcher");
            System.out.println("i had broadcast");
        }, 10, TimeUnit.SECONDS);

//        BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
//        while (true) {
//            String in = sysin.readLine();
//            server.broadcast(in);
//            if (in.equals("exit")) {
//                server.stop(1000);
//                break;
//            }
//        }
    }

    public void stop() throws IOException, InterruptedException {
        server.stop();
    }

    public void broadcast(byte[] data) {
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
