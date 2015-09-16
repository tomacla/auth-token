package io.github.tomacla.auth.server.core.provider;

public class AlwaysTrueProvider implements AccountProvider {

    @Override
    public Boolean authenticate(String login, String password) {
	return true;
    }

}
