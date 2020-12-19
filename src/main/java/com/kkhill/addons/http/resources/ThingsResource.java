package com.kkhill.addons.http.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    public byte[] things() throws JsonProcessingException {

        Map<String, Thing> things = Catcher.getThingMonitor().getThings();
        List<Object> data = new ArrayList<>();
        for(String key : things.keySet()) {
            data.add(ThingUtil.extractThing(things.get(key)));
        }

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsBytes(data);
    }
}