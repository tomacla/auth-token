package io.github.tomacla.common.security.provider;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import io.github.tomacla.common.security.authentication.TokenAuthentication;
import io.github.tomacla.common.security.token.TokenDTO;
import io.github.tomacla.common.service.TokenService;

public class TokenAuthenticationProviderTest {

    private TokenService authService;
    private UserDetailsService userDetailsService;
    private TokenAuthenticationProvider authProvider;

    @Before
    public void before() {
	this.authService = Mockito.mock(TokenService.class);
	this.userDetailsService = Mockito.mock(UserDetailsService.class);
	this.authProvider = new TokenAuthenticationProvider(authService, userDetailsService);
    }

    @Test
    public void authenticate() {
	User fakeUser = new User("john", "doe", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
	TokenDTO tokenData = new TokenDTO();
	tokenData.setEmail("john.doe@sample.org");
	Mockito.when(authService.verify("token")).thenReturn(Optional.of(tokenData));
	Mockito.when(userDetailsService.loadUserByUsername("john.doe@sample.org")).thenReturn(fakeUser);
	TokenAuthentication authentication = new TokenAuthentication("token");
	TokenAuthentication authenticate = (TokenAuthentication) this.authProvider.authenticate(authentication);
	Assert.assertEquals("token", authenticate.getToken());
	Assert.assertEquals(1, authenticate.getAuthorities().size());
    }

    @Test(expected = BadCredentialsException.class)
    public void authenticateWithInvalidToken() {
	Mockito.when(authService.verify("token")).thenReturn(Optional.empty());
	TokenAuthentication authentication = new TokenAuthentication("token");
	this.authProvider.authenticate(authentication);
    }

    @Test(expected = BadCredentialsException.class)
    public void authenticateWithNullToken() {
	TokenAuthentication authentication = new TokenAuthentication(null);
	this.authProvider.authenticate(authentication);
    }

    @Test
    public void authenticateWithUserName() {
	UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("foo", "bar");
	Authentication authenticate = this.authProvider.authenticate(authentication);
	Assert.assertNull(authenticate);
    }

    @Test
    public void supports() {
	Assert.assertTrue(this.authProvider.supports(TokenAuthentication.class));
    }

}
