package com.kkhill.addons.http.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class TestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(200, 0);
        OutputStream response = httpExchange.getResponseBody();
        response.write("test http server".getBytes());
        response.close();
    }
}
