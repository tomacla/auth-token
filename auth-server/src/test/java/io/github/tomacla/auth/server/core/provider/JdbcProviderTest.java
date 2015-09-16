package io.github.tomacla.auth.server.core.provider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JdbcProviderTest {

    private AccountProvider provider;
    
    @Before
    public void before() {
	provider = new JdbcProvider();
    }
    
    @Test
    public void authenticate() {
	Assert.assertFalse(provider.authenticate("foo", "bar"));
    }
    
    
}
