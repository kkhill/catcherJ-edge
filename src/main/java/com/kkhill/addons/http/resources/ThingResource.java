package com.kkhill.addons.http.resources;

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
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object thing(String id) {

        Object data;
        try {
            data = ThingUtil.extractThingDTO(Catcher.getThingMonitor().getThing(id));
        } catch (NotFoundException e) {
            data = "no such thing";
        }
        return data;
    }
}
