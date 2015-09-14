package io.github.tomacla.common.service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
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

}
