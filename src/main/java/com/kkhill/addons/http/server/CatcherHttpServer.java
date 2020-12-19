package com.kkhill.addons.http.server;

import com.kkhill.core.annotation.State;
import com.kkhill.core.thing.Thing;
import com.kkhill.utils.thing.StateName;
import com.kkhill.utils.thing.ThingType;

import com.sun.net.httpserver.HttpServer;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * use HttpServer(JDK) and Jersey to provide rest services
 */
// TODO: json serialization
public class CatcherHttpServer extends Thing {

    private HttpServer server;
    private ResourceConfig config;
    private List<String> resList;
    private String host = "localhost";
    private int port = 8000;

    @State
    private String state = StateName.OFF;

    public CatcherHttpServer() {
        super(ThingType.ADDON_HTTP, "catcher http server", "http server");

        this.setId("httpserver"); // to be used by other plugin
        resList = new ArrayList<>();
    }

    public void start() throws IOException {
        String[] s = new String[resList.size()];
        for(int i=0; i<s.length; i++) s[i] = resList.get(i);
        config = new PackagesResourceConfig(s);
        server = HttpServerFactory.create(getURI(), config);
        server.start();
    }

    public void addResource(String res) {
        resList.add(res);
    }

    private URI getURI() {
        return UriBuilder.fromUri("http://"+host+"/").port(port).build();
    }
}
