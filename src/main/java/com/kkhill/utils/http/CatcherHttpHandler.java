package com.kkhill.utils.http;

import com.kkhill.addons.rulengine.RuleEngine;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

public class CatcherHttpHandler implements HttpHandler {

    private final Logger logger = LoggerFactory.getLogger(CatcherHttpHandler.class);

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        if(method.equalsIgnoreCase("GET")) {
            get(httpExchange);
        } else if(method.equalsIgnoreCase("POST")) {
            post(httpExchange);
        } else if(method.equalsIgnoreCase("PUT")) {
            put(httpExchange);
        } else if(method.equalsIgnoreCase("DELETE")) {
            delete(httpExchange);
        }
    }

    public void get(HttpExchange httpExchange) throws IOException {
        logger.error("GET method is not implemented");
    }
    public void post(HttpExchange httpExchange) throws IOException {
        logger.error("POST method is not implemented");
    }
    public void delete(HttpExchange httpExchange) throws IOException {
        logger.error("DELETE method is not implemented");
    }
    public void put(HttpExchange httpExchange) throws IOException {
        logger.error("PUT method is not implemented");
    }
}
