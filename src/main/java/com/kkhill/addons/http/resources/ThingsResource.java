package com.kkhill.addons.http.resources;

import com.kkhill.addons.http.utils.ThingUtil;
import com.kkhill.core.Catcher;
import com.kkhill.core.thing.Thing;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Path("/things")
public class ThingsResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Object> things() {

        Map<String, Thing> things = Catcher.getThingMonitor().getThings();
        List<Object> data = new ArrayList<>();
        for(String key : things.keySet()) {
            data.add(ThingUtil.extractThingDTO(things.get(key)));
        }
        return data;
    }
}