package io.github.tomacla.common.service;

import java.util.Arrays;
import java.util.Optional;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import io.github.tomacla.common.security.token.ReadOnlyTokenManager;
import io.github.tomacla.common.security.token.TokenDTO;

public class DefaultTokenService implements TokenService {

    protected static final String PROTECTED_PASSWORD = "[PROTECTED]";
    
    private Client client;
    private ReadOnlyTokenManager tokenManager;
    private String authServerRootPath;

    public DefaultTokenService(ReadOnlyTokenManager tokenManager, String authServerRootPath) {
	client = ClientBuilder.newClient();
	this.tokenManager = tokenManager;
	this.setAuthServerRootPath(authServerRootPath);
    }

    @Override
    public Optional<TokenDTO> verify(String token) {
	return tokenManager.readToken(token);
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	return new User(username, PROTECTED_PASSWORD, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
    }

}
