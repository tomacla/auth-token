package io.github.tomacla.auth.server.core.service;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import io.github.tomacla.auth.server.core.service.DefaultAccountService;

@RunWith(MockitoJUnitRunner.class)
public class DefaultAccountServiceTest {

    @InjectMocks
    private DefaultAccountService service;

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
    public void verifyToken() {
	Optional<String> token = this.service.authenticate("tomacla", "tomaclapwd");
	Optional<String> result = this.service.verifyToken(token.get());
	Assert.assertTrue(result.isPresent());
	Assert.assertEquals("tomacla", result.get());
	this.service.invalidateToken(token.get());
	result = this.service.verifyToken(token.get());
	Assert.assertFalse(result.isPresent());
    }
    
    @Test
    public void authCodes() {
	String authCode = this.service.getAuthCodeForToken("foo");
	Assert.assertNotNull(authCode);
	Optional<String> token = this.service.getTokenFromAuthCode(authCode);
	Assert.assertTrue(token.isPresent());
	Assert.assertEquals("foo", token.get());
    }
    
    @Test
    public void authCodesUnknown() {
	Optional<String> token = this.service.getTokenFromAuthCode("foo");
	Assert.assertFalse(token.isPresent());
    }
    

}
