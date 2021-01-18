package com.kkhill.addons.http.server;

import com.kkhill.core.annotation.State;
import com.kkhill.core.thing.Thing;
import com.kkhill.common.thing.StateName;
import com.kkhill.common.thing.ThingType;
import io.netty.channel.Channel;
import org.glassfish.jersey.netty.httpserver.NettyHttpContainerProvider;
import org.glassfish.jersey.server.ResourceConfig;


import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * use HttpServer(JDK) and Jersey to provide rest services
 */
public class CatcherHttpServer extends Thing {

    private Channel server;
    public ResourceConfig resourceConfig;
    private String host;
    private int port;

    @State
    public String state = StateName.ON;

    public CatcherHttpServer(String host, int port) {
        super(ThingType.ADDON_HTTP, "http_server", "http server");
        this.host = host;
        this.port = port;
        this.setId("httpserver"); // to be used by other plugin
        resourceConfig = new ResourceConfig();
    }

    public void start() {
        server = NettyHttpContainerProvider.createHttp2Server(getURI(), resourceConfig, null);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> server.close()));
    }

    public void stop() {
        server.close();
    }

    private URI getURI() {
        return UriBuilder.fromUri("http://"+host+"/").port(port).build();
    }
}
