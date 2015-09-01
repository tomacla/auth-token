package com.thomasclarisse.auth.server.service;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {

	@InjectMocks
	private AuthenticationService service;
	
	@Test
	public void authenticate() {
		Optional<String> result = this.service.authenticate("tomacla", "tomaclapwd");
		Assert.assertTrue(result.isPresent());
	}
	
	@Test
	public void authenticate_unknownLogin() {
		Optional<String> result = this.service.authenticate("foo", "tomaclapwd");
		Assert.assertFalse(result.isPresent());
	}
	
	@Test
	public void authenticate_unknownPassword() {
		Optional<String> result = this.service.authenticate("tomacla", "bar");
		Assert.assertFalse(result.isPresent());
	}
	
	@Test
	public void authenticate_unknownCredentials() {
		Optional<String> result = this.service.authenticate("foo", "bar");
		Assert.assertFalse(result.isPresent());
	}
	
	@Test
	public void getTokenFromAuthCode() {
		Optional<String> authCode = this.service.authenticate("tomacla", "tomaclapwd");
		Optional<String> result = this.service.getTokenFromAuthCode(authCode.get());
		Assert.assertTrue(result.isPresent());
	}
	
	@Test
	public void getTokenFromAuthCode_unknownCode() {
		Optional<String> result = this.service.getTokenFromAuthCode("foo");
		Assert.assertFalse(result.isPresent());
	}
	
	@Test
	public void verifyToken() {
		Optional<String> authCode = this.service.authenticate("tomacla", "tomaclapwd");
		Optional<String> token = this.service.getTokenFromAuthCode(authCode.get());
		Optional<String> result = this.service.verifyToken(token.get());
		Assert.assertTrue(result.isPresent());
		Assert.assertEquals("tomacla", result.get());
		this.service.invalidateToken(token.get());
		result = this.service.verifyToken(token.get());
		Assert.assertFalse(result.isPresent());
	}
	
}
