package com.kkhill.addons.http.server;

import com.kkhill.core.Catcher;
import com.kkhill.core.annotation.Service;
import com.kkhill.core.annotation.State;
import com.kkhill.core.thing.Thing;
import com.kkhill.utils.thing.StateName;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * use HttpServer in JDK to provide api services
 */
public class CatcherHttpServer extends Thing {

    public HttpServer server;

    @State
    private String state = StateName.OFF;

    @Service(name = "route")
    private void route(String path, HttpHandler handler) {
        this.server.createContext(path, handler);
    }

    public CatcherHttpServer(String friendlyName, boolean available) throws IOException {
        super(friendlyName, available);

        HttpServerProvider provider = HttpServerProvider.provider();
        server = provider.createHttpServer(new InetSocketAddress(8080), 100);
        server.setExecutor(Catcher.getScheduler().getExecutor());
        server.start();
    }
}
