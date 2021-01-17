package com.kkhill.common.thing;

import com.kkhill.core.thing.ServiceParam;
import com.kkhill.core.thing.Thing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThingUtil {

    public static Map<String, Object> buildThingDTO(Thing thing) {

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

        return t;
    }


}
