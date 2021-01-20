package com.kkhill.addons.http.resources;

import com.kkhill.common.http.ServiceParamsDTO;
import com.kkhill.common.thing.ThingUtil;
import com.kkhill.core.Catcher;
import com.kkhill.core.exception.NotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/thing")
public class ThingResource {

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getThing(@PathParam("id") String id) {

        Object res;
        try {
            res = ThingUtil.serializeThing(Catcher.getThingMonitor().getThing(id));
        } catch (NotFoundException e) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
        }
        return Response.status(Response.Status.OK).entity(res).build();
    }

    @POST
    @Path("{id}/call/{service}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response callService(
            @PathParam("id") String id,
            @PathParam("service") String service,
            List<Map<String, Object>> data) {

        try {
            // deserialize service args based on field 'type' in data
            Map<String, Object> args = new HashMap<>();
            for(Map<String, Object> d : data) {
                byte[] t = (byte[])d.get("name");

//                String name = (String)ThingUtil.deserializeServiceParams((byte[])d.get("name"), "String");
//                String type = (String)ThingUtil.deserializeServiceParams((byte[])d.get("type"), "String");
//                Object value = ThingUtil.deserializeServiceParams((byte[])d.get("value"), type);
//                args.put(name, value);
            }
            System.out.println("test");
            Object res = Catcher.getThingMonitor().callServiceAndNotify(id, service, null);
            return Response.status(Response.Status.OK).entity(res).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

    }
}
