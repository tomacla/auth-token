package io.github.tomacla.common.security.authentication;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class TokenAuthentication extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 1L;

    private String token;

    public TokenAuthentication(String token) {
	this(token, null);
    }

    public TokenAuthentication(String token, Collection<? extends GrantedAuthority> authorities) {
	super(authorities);
	this.token = token;
    }

    @Override
    public Object getCredentials() {
	// TODO do something
	return null;
    }

    @Override
    public Object getPrincipal() {
	// TODO do something
	return null;
    }

    public String getToken() {
	return token;
    }

}