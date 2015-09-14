package io.github.tomacla.common.security.authentication;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import io.github.tomacla.common.security.authentication.TokenAuthentication;

public class TokenAuthenticationTest {

    @Test
    public void construct() {
	TokenAuthentication t = new TokenAuthentication("foo");
	Assert.assertEquals("foo", t.getToken());
    }

    @Test
    public void constructWithAuthorities() {
	TokenAuthentication t = new TokenAuthentication("foo", Arrays.asList(new SimpleGrantedAuthority("TEST")));
	Assert.assertEquals("foo", t.getToken());
	Assert.assertEquals(1, t.getAuthorities().size());
    }

    @Test
    public void getCredentials() {
	TokenAuthentication t = new TokenAuthentication("foo");
	Assert.assertNull(t.getCredentials());
    }

    @Test
    public void getPrincipal() {
	TokenAuthentication t = new TokenAuthentication("foo");
	Assert.assertNull(t.getPrincipal());
    }

}