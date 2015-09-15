package io.github.tomacla.common.service;

import java.util.Optional;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class DefaultTokenService implements TokenService {

    private Client client;

    public DefaultTokenService() {
	client = ClientBuilder.newClient();
    }

    @Override
    public Boolean verify(String token) {
	// TODO use env variable
	WebTarget target = client.target("http://localhost:8080/auth-server").path("/api/" + token);
	Response r = target.request().get();
	try {
	    if (r.getStatusInfo().equals(Status.OK)) {
		return true;
	    }
	    return false;
	} finally {
	    r.close();
	}
    }

    @Override
    public Optional<String> getTokenFromAuthCode(String authCode) {
	// TODO use env variable
	WebTarget target = client.target("http://localhost:8080/auth-server").path("/api/auth-code");
	Response r = target.request().post(Entity.json(authCode));
	try {
	    if (r.getStatusInfo().equals(Status.OK)) {
		return Optional.of(r.readEntity(String.class));
	    }
	    return Optional.empty();
	} finally {
	    r.close();
	}
    }

}
