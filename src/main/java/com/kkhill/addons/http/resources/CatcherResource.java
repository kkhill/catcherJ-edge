package com.kkhill.addons.http.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/catcher")
public class CatcherResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void refresh() {
        
    }
}
