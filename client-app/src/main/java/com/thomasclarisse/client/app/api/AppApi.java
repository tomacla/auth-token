package com.thomasclarisse.client.app.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/")
public class AppApi {

	@GET
	public Response get() {
		return Response.ok("hello world").build();
	}

}
