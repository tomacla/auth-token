package io.github.tomacla.server.app.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/hello")
public class HelloWorldApi {

    @GET
    public Response get() {
	return Response.ok("Hello world").build();
    }

}