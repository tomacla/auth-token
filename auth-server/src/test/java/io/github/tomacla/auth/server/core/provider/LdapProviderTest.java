package io.github.tomacla.auth.server.core.provider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LdapProviderTest {

    private AccountProvider provider;
    
    @Before
    public void before() {
	provider = new LdapProvider();
    }
    
    @Test
    public void authenticate() {
	Assert.assertFalse(provider.authenticate("foo", "bar"));
    }
    
    
}
