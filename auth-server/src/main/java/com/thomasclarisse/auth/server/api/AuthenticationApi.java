package com.thomasclarisse.auth.server.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Created by Thomas on 31/08/2015.
 */
@Path("/auth")
public class AuthenticationApi {

    @GET
    public Response get() {
        return Response.ok().build();
    }

}
