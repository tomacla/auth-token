package com.thomasclarisse.auth.server.api;

import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.http.MediaType;

import com.thomasclarisse.auth.server.api.dto.AuthRequestDTO;
import com.thomasclarisse.auth.server.service.AuthenticationService;

@Path("/auth")
public class AuthenticationApi {

	private AuthenticationService authService; 

	@Inject
	public AuthenticationApi(AuthenticationService authService) {
		this.authService = authService;
	}

	@GET
	public Response get() {
		return Response.ok().build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.TEXT_PLAIN_VALUE)
	public Response authenticate(@DefaultValue("token") @QueryParam("resultType") String resultType, AuthRequestDTO authRequest) {
		Optional<String> authCode = authService.authenticate(authRequest.login, authRequest.password);
		if(authCode.isPresent()) {
			if(resultType.equals("token")) {
				Optional<String> tokenFromAuthCode = authService.getTokenFromAuthCode(authCode.get());
				if(tokenFromAuthCode.isPresent()) {
					return Response.ok(tokenFromAuthCode.get()).build();
				}
				return Response.serverError().build();
			}
			else if(resultType.equals("code")) {
				return Response.ok(authCode.get()).build();
			}
		}
		return Response.status(Status.BAD_REQUEST).build();
	}
	
}
