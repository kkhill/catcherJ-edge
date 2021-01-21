package com.kkhill.addons.http.server;

import com.kkhill.core.annotation.Service;
import com.kkhill.core.annotation.ServiceParam;
import com.kkhill.core.annotation.State;
import com.kkhill.core.thing.AddonThing;
import com.kkhill.common.thing.CommonState;
import io.netty.channel.Channel;
import org.glassfish.jersey.netty.httpserver.NettyHttpContainerProvider;
import org.glassfish.jersey.server.ResourceConfig;


import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * use NettyHttp and Jersey to provide rest services
 */
public class CatcherHttpServer extends AddonThing {

    private Channel server;
    public ResourceConfig resourceConfig;
    private String host;
    private int port;

    @State
    public String state = CommonState.ON;

    public CatcherHttpServer(String host, int port) {
        super("server", "http server");
        this.host = host;
        this.port = port;
        resourceConfig = new ResourceConfig();
    }

    @Service(name="addResources", description = "add resource packages to expose web services")
    public void addResources(@ServiceParam(name="packages", description = "resource packages") String... packages) {
        this.resourceConfig.packages(packages);
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
