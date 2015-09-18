package io.github.tomacla.auth.server.core.provider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InMemoryProviderTest {

    private InMemoryProvider provider;
    
    @Before
    public void before() {
	provider = new InMemoryProvider("users.json");
    }
    
    @Test
    public void authenticate() {
	Assert.assertTrue(provider.authenticate("test1", "test1"));
    }
    
    @Test
    public void authenticateOtherUser() {
	Assert.assertTrue(provider.authenticate("test2", "test2"));
    }
    
    @Test
    public void authenticateFalse() {
	Assert.assertFalse(provider.authenticate("test1", "aaa"));
    }
    
    @Test
    public void authenticateLoginFalse() {
	Assert.assertFalse(provider.authenticate(null, "aaa"));
    }
    
    @Test
    public void authenticatePasswordFalse() {
	Assert.assertFalse(provider.authenticate("aaa", null));
    }
    
}
