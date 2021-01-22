package com.kkhill.common.thing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kkhill.core.thing.Service;
import com.kkhill.core.thing.ServiceParam;
import com.kkhill.core.thing.Thing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThingUtil {

    public static Map<String, Object> serializeThing(Thing thing) {

        Map<String, Object> t = new HashMap<>();
        t.put("id", thing.getId());
        t.put("name", thing.getFriendlyName());
        t.put("type", thing.getType());
        t.put("state", thing.getState().getValue());
        t.put("description", thing.getDescription());
        t.put("available", thing.isAvailable());

        // construct properties
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

        // construct services
        List<Map<String, Object>> services = new ArrayList<>();
        for(String name : thing.getServices().keySet()) {
            Service s = thing.getService(name);
            // do not expose poll service
            if(s.isPolled()) continue;
            Map<String, Object> service = new HashMap<>();
            service.put("name", name);
            service.put("description", s.getDescription());
            service.put("interval", s.getPollInterval());
            List<Map<String, String>> parameters = new ArrayList<>();
            for(ServiceParam sp : s.getParameters()) {
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

        return t;
    }

    /**
     * deserialize dynamic java basic type based on type(String)
     * @param value
     * @param type
     * @return
     */
    public static Object deserializeServiceParams(Object value, String type) {

        // TODO array type
        ObjectMapper objectMapper = new ObjectMapper();
        if("int".equals(type) || "Integer".equals(type)) {
            return objectMapper.convertValue(value, Integer.class);
        } else if("short".equals(type) || "Short".equals(type)) {
            return objectMapper.convertValue(value, Short.class);
        } else if("long".equals(type) || "Long".equals(type)) {
            return objectMapper.convertValue(value, Long.class);
        } else if("double".equals(type) || "Double".equals(type)) {
            return objectMapper.convertValue(value, Double.class);
        } else if("boolean".equals(type) || "Boolean".equals(type)) {
            return objectMapper.convertValue(value, Boolean.class);
        } else {
            return objectMapper.convertValue(value, String.class);
        }
    }


}
