package com.kkhill.addons.http.resources;

import com.kkhill.addons.http.utils.ThingUtil;
import com.kkhill.core.Catcher;
import com.kkhill.core.exception.NotFoundException;
import org.glassfish.jersey.server.ContainerRequest;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import java.util.Map;

@Path("/thing")
public class ThingResource {

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object thing(@PathParam("id") String id) {

        Object data;
        try {
            data = ThingUtil.extractThingDTO(Catcher.getThingMonitor().getThing(id));
        } catch (NotFoundException e) {
            data = "no such thing: " + id;
        }
        return data;
    }

    @POST
    @Path("{id}/service/{service}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Object callService(@PathParam("id") String id, @PathParam("service") String service, Object args) {

        Object data;
        try {
            data = Catcher.getThingMonitor().callServiceAndNotify(id, service, args);
        } catch (NotFoundException e) {
            data = "failed to call service: " + id + "." + service;
        }

        return data;
    }
}
