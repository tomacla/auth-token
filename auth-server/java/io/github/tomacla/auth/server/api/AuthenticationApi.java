package io.github.tomacla.auth.server.api;

import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import io.github.tomacla.auth.server.api.dto.AuthRequestDTO;
import io.github.tomacla.auth.server.core.service.DefaultAccountService;

@Path("/")
public class AuthenticationApi {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationApi.class);
    
    private DefaultAccountService authService;

    @Inject
    public AuthenticationApi(DefaultAccountService authService) {
	this.authService = authService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.TEXT_PLAIN_VALUE)
    public Response authenticateByCredentials(AuthRequestDTO authRequest) {
	LOGGER.debug("Trying to authenticate {}", authRequest.login);
	Optional<String> token = authService.authenticate(authRequest.login, authRequest.password);
	if (token.isPresent()) {
	    return Response.ok(token.get()).build();
	}
	return Response.status(Status.BAD_REQUEST).build();
    }

    @GET
    @Path("/{token}")
    public Response verifyToken(@PathParam("token") String token) {
	LOGGER.debug("Trying to verify {}", token);
	Optional<String> verified = authService.verifyToken(token);
	if (verified.isPresent()) {
	    return Response.ok().build();
	}
	return Response.status(Status.BAD_REQUEST).build();
    }
    
    @POST
    @Path("/auth-code")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.TEXT_PLAIN_VALUE)
    public Response getTokenFromAuthCode(String authCode) {
	Optional<String> tokenFromAuthCode = this.authService.getTokenFromAuthCode(authCode);
	if(tokenFromAuthCode.isPresent()) {
	    return Response.ok(tokenFromAuthCode.get()).build();
	}
	return Response.status(Status.BAD_REQUEST).build();
    }

}
