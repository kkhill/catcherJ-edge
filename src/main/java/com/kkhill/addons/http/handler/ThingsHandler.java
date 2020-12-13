package com.kkhill.addons.http.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkhill.core.Catcher;
import com.kkhill.core.thing.Thing;
import com.kkhill.utils.http.CatcherHttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThingsHandler extends CatcherHttpHandler {

    public void get(HttpExchange httpExchange) throws IOException {

        Map<String, Thing> things = Catcher.getThingMonitor().getThings();
        List<Object> data = new ArrayList<>();
        for(String key : things.keySet()) {
            Thing thing = things.get(key);
            Map<String, Object> t = new HashMap<>();
            t.put("id", thing.getId());
            t.put("name", thing.getFriendlyName());
            List<Map<String, String>> props = new ArrayList<>();
            for(String p : thing.getProperties().keySet()) {
                Map<String, String> prop = new HashMap<>();
                prop.put("name", thing.getProperties().get(p).getName());
                prop.put("description", thing.getProperties().get(p).getDescription());
                prop.put("value", String.valueOf(thing.getProperties().get(p).getValue()));
                prop.put("unitOfMeasurement", thing.getProperties().get(p).getUnitOfMeasurement());
                props.add(prop);
            }
            t.put("properties", props);

            data.add(t);
        }
        ObjectMapper mapper = new ObjectMapper();
        OutputStream response = httpExchange.getResponseBody();
        response.write(mapper.writeValueAsBytes(data));

        httpExchange.sendResponseHeaders(200, 0);
        response.close();
    }
}
