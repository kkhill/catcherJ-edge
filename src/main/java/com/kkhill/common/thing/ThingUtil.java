package com.kkhill.common.thing;

import com.kkhill.core.thing.ServiceParam;
import com.kkhill.core.thing.Thing;

import java.io.UnsupportedEncodingException;
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

        return t;
    }

    public static Object deserializeServiceParams(byte[] value, String type) throws UnsupportedEncodingException {
        String s = new String(value, "UTF-8");
        if("int".equals(type) || "Integer".equals(type)) {
            return Integer.valueOf(s);
        } else if("short".equals(type) || "Short".equals(type)) {
            return Short.valueOf(s);
        } else if("long".equals(type) || "Long".equals(type)) {
            return Long.valueOf(s);
        } else if("double".equals(type) || "Double".equals(type)) {
            return Double.valueOf(s);
        } else if("boolean".equals(type) || "Boolean".equals(type)) {
            return Boolean.valueOf(s);
        } else {
            return s;
        }
    }


}