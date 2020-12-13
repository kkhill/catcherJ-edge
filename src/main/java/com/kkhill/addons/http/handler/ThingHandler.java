package com.kkhill.addons.http.handler;

import com.kkhill.utils.http.CatcherHttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class ThingHandler extends CatcherHttpHandler {

    public void post(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(200, 0);
        OutputStream response = httpExchange.getResponseBody();
        response.write("test get http server".getBytes());
        response.close();
    }
}
