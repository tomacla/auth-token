package io.github.tomacla.auth.server.core.service;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.github.tomacla.auth.server.core.provider.AccountProvider;
import io.github.tomacla.common.security.token.TokenManager;

public class DefaultAccountServiceTest {

    private DefaultAccountService service;
    private AccountProvider provider;
    private TokenManager manager;
    
    @Before
    public void before() {
	this.manager = Mockito.mock(TokenManager.class);
	this.provider = Mockito.mock(AccountProvider.class);
	this.service = new DefaultAccountService(manager, Arrays.asList(this.provider));
    }

    @Test
    public void authenticate() {
	Mockito.when(manager.getToken(Mockito.any())).thenReturn("token");
	Mockito.when(provider.authenticate("tomacla", "tomaclapwd")).thenReturn(true);
	Optional<String> result = this.service.authenticate("tomacla", "tomaclapwd");
	Assert.assertTrue(result.isPresent());
    }

    @Test
    public void authenticate_unknownLogin() {
	Mockito.when(provider.authenticate("tomacla", "tomaclapwd")).thenReturn(false);
	Optional<String> result = this.service.authenticate("tomacla", "tomaclapwd");
	Assert.assertFalse(result.isPresent());
    }

    @Test
    public void verifyToken() {
	Mockito.when(manager.isValid("token")).thenReturn(true);
	Boolean result = this.service.verifyToken("token");
	Assert.assertTrue(result);
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
