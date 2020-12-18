package com.kkhill.addons.http.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkhill.core.Catcher;
import com.kkhill.core.thing.Service;
import com.kkhill.core.thing.ServiceParam;
import com.kkhill.core.thing.Thing;
import com.kkhill.utils.http.CatcherHttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Parameter;
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
            t.put("type", thing.getType());
            t.put("state", thing.getState().getValue());

            List<Map<String, String>> props = new ArrayList<>();
            for(String p : thing.getProperties().keySet()) {
                Map<String, String> prop = new HashMap<>();
                prop.put("name", p);
                prop.put("description", thing.getProperties().get(p).getDescription());
                prop.put("value", String.valueOf(thing.getProperties().get(p).getValue()));
                prop.put("unitOfMeasurement", thing.getProperties().get(p).getUnitOfMeasurement());
                props.add(prop);
            }
            t.put("properties", props);

            List<Map<String, Object>> services = new ArrayList<>();
            for(String s : thing.getServices().keySet()) {
                Map<String, Object> service = new HashMap<>();
                service.put("name", s);
                service.put("description", thing.getService(s).getDescription());
                List<Map<String, String>> parameters = new ArrayList<>();
                for(ServiceParam sp : thing.getService(s).getParameters()) {
                    Map<String, String> m = new HashMap<>();
                    m.put("name", sp.getName());
                    m.put("type", sp.getType());
                    m.put("description", sp.getDescription());
                    parameters.add(m);
                }
                service.put("parameters", parameters);
                services.add(service);
            }
            t.put("services", services);

            data.add(t);
        }

        httpExchange.sendResponseHeaders(200, 0);
        OutputStream response = httpExchange.getResponseBody();
        ObjectMapper mapper = new ObjectMapper();
        response.write(mapper.writeValueAsBytes(data));

        response.close();
    }
}
