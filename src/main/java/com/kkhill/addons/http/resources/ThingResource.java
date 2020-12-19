package com.kkhill.addons.http.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.kkhill.addons.http.utils.ThingUtil;
import com.kkhill.core.Catcher;
import com.kkhill.core.exception.NotFoundException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/thing")
public class ThingResource {

    @GET
//    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public TestPOJO thing(String id) throws JsonProcessingException {

//        Object data;
//        try {
//            data = ThingUtil.extractThing(Catcher.getThingMonitor().getThing(id));
//        } catch (NotFoundException e) {
//            data = "no such thing";
//        }
//        ObjectMapper mapper = new ObjectMapper();
//        return mapper.writeValueAsBytes(data);
        return new TestPOJO("123");
    }

}
