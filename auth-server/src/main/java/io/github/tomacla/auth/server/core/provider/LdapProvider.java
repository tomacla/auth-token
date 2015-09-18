package io.github.tomacla.auth.server.core.provider;

public class LdapProvider implements AccountProvider {

    @Override
    public Boolean authenticate(String login, String password) {
	throw new UnsupportedOperationException("LDAP provider is not implemented yet");
    }

}
