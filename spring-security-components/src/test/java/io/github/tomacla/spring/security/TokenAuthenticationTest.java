package io.github.tomacla.spring.security;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import io.github.tomacla.spring.security.TokenAuthentication;

public class TokenAuthenticationTest {

    @Test
    public void construct() {
	TokenAuthentication t = new TokenAuthentication("foo");
	Assert.assertEquals("foo", t.getToken());
    }

    @Test
    public void constructWithAuthorities() {
	User fake = new User("john", "doe", Arrays.asList(new SimpleGrantedAuthority("TEST")));
	TokenAuthentication t = new TokenAuthentication("foo", fake);
	Assert.assertEquals("foo", t.getToken());
	Assert.assertEquals(1, t.getAuthorities().size());
    }

    @Test
    public void getCredentials() {
	TokenAuthentication t = new TokenAuthentication("foo");
	Assert.assertEquals("foo", t.getCredentials());
    }

    @Test
    public void getPrincipal() {
	User fake = new User("john", "doe", Arrays.asList(new SimpleGrantedAuthority("TEST")));
	TokenAuthentication t = new TokenAuthentication("foo", fake);
	Assert.assertEquals(fake, t.getPrincipal());
    }
    
    @Test
    public void getPrincipalNull() {
	TokenAuthentication t = new TokenAuthentication("foo");
	Assert.assertNull(t.getPrincipal());
    }

}