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
    private String authServerRootPath;

    public DefaultTokenService(String authServerRootPath) {
	client = ClientBuilder.newClient();
	this.setAuthServerRootPath(authServerRootPath);
    }

    @Override
    public Boolean verify(String token) {
	
	// TODO JWT can be check without requesting auth server. just need the key
	
	WebTarget target = client.target(authServerRootPath).path("/api/" + token);
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
	WebTarget target = client.target(authServerRootPath).path("/api/auth-code");
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

    private void setAuthServerRootPath(String authServerRootPath) {
	this.authServerRootPath = authServerRootPath;
	if (this.authServerRootPath.endsWith("/")) {
	    this.authServerRootPath = this.authServerRootPath.substring(0, this.authServerRootPath.length() - 1);
	}
    }

}
