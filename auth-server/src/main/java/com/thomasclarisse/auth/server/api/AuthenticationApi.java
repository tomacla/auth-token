package com.thomasclarisse.auth.server.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
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
	@Path("/login")
	@Produces(MediaType.TEXT_HTML_VALUE)
	public Response displayLoginForm(@HeaderParam("Referer") String referer) {
		try {
			if(referer == null) {
				referer = "http://www.google.fr";
			}
			ByteArrayOutputStream bas = new ByteArrayOutputStream();
			java.nio.file.Path path = Paths.get(getClass().getClassLoader().getResource("login.html").toURI());
			Files.copy(path, bas);
			String loginPageAsString = bas.toString();
			loginPageAsString = loginPageAsString.replaceAll("REDIRECT_URI_TO_REPLACE", referer);
			return Response.ok(loginPageAsString).build();
		} catch (URISyntaxException | IOException e) {
			return Response.serverError().build();
		}
	}

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@Produces(MediaType.TEXT_PLAIN_VALUE)
	public Response receiveLoginForm(@FormParam("login") String login, @FormParam("password") String password, @FormParam("redirectTo") String redirectTo) {
		Optional<String> authCode = authService.authenticate(login, password);
		if(authCode.isPresent()) {
			return Response.ok(redirectTo + "?code="+authCode.get()).build();
		}
		return Response.status(Status.BAD_REQUEST).build();
	}

	@POST
	@Path("/credentials")
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.TEXT_PLAIN_VALUE)
	public Response authenticateByCredentials(@DefaultValue("token") @QueryParam("resultType") String resultType, AuthRequestDTO authRequest) {
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

	@POST
	@Path("/code")
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	@Produces(MediaType.TEXT_PLAIN_VALUE)
	public Response authenticateByCode(String authCode) {
		Optional<String> token = authService.getTokenFromAuthCode(authCode);
		if(token.isPresent()) {
			return Response.ok(token.get()).build();
		}
		return Response.status(Status.BAD_REQUEST).build();
	}

}
