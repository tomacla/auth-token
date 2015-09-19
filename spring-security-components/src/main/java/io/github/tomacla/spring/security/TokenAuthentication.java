package io.github.tomacla.spring.security;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class TokenAuthentication implements Authentication {

    private static final long serialVersionUID = 1L;

    private Boolean authenticated;
    private String token;
    private UserDetails userDetails;

    public TokenAuthentication(String token) {
	this(token, null);
    }

    public String getToken() {
        return token;
    }

    public TokenAuthentication(String token, UserDetails userDetails) {
	this.token = token;
	this.userDetails = userDetails;
	this.authenticated = false;
    }

    @Override
    public Object getPrincipal() {
	return this.userDetails;
    }

    @Override
    public String getName() {
	return this.userDetails.getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
	return this.userDetails.getAuthorities();
    }

    @Override
    public Object getDetails() {
	return this.userDetails;
    }

    @Override
    public boolean isAuthenticated() {
	return this.authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
	this.authenticated = isAuthenticated;
    }

    @Override
    public Object getCredentials() {
	return this.token;
    }

}