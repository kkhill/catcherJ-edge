package com.kkhill.common.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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
        logger.debug("GET method is not implemented");
    }
    public void post(HttpExchange httpExchange) throws IOException {
        logger.debug("POST method is not implemented");
    }
    public void delete(HttpExchange httpExchange) throws IOException {
        logger.debug("DELETE method is not implemented");
    }
    public void put(HttpExchange httpExchange) throws IOException {
        logger.debug("PUT method is not implemented");
    }
}
