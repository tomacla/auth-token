package io.github.tomacla.auth.server.core.provider;

import org.junit.Before;
import org.junit.Test;

public class LdapProviderTest {

    private AccountProvider provider;
    
    @Before
    public void before() {
	provider = new LdapProvider();
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void authenticate() {
	provider.authenticate("foo", "bar");
    }
    
    
}
