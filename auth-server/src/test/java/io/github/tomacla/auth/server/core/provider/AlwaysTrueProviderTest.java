package io.github.tomacla.auth.server.core.provider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AlwaysTrueProviderTest {

    private AccountProvider provider;
    
    @Before
    public void before() {
	provider = new AlwaysTrueProvider();
    }
    
    @Test
    public void authenticate() {
	Assert.assertTrue(provider.authenticate("foo", "bar"));
    }
    
    @Test
    public void authenticate_null() {
	Assert.assertTrue(provider.authenticate(null, null));
    }
    
}
