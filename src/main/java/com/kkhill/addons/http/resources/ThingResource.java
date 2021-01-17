package com.kkhill.addons.http.resources;

import com.kkhill.common.thing.ThingUtil;
import com.kkhill.core.Catcher;
import com.kkhill.core.exception.NotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@Path("/thing")
public class ThingResource {

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object thing(@PathParam("id") String id) {

        Object res;
        try {
            res = ThingUtil.buildThingDTO(Catcher.getThingMonitor().getThing(id));
        } catch (NotFoundException e) {
            res = "no such thing: " + id;
        }
        return res;
    }

    @POST
    @Path("{id}/call/{service}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Object callService(@PathParam("id") String id, @PathParam("service") String service, Map<String, Object> data) {

        Object res;
        try {
            res = Catcher.getThingMonitor().callServiceAndNotify(id, service, data == null ? null : data.values().toArray());
        } catch (NotFoundException e) {
            res = "service not found: " + id + "." + service;
        } catch (Exception e) {
            e.printStackTrace();
            res = "unknown error, please check your args";
        }
        if(res == null) res = "success";
        return res;
    }
}
