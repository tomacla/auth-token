package io.github.tomacla.auth.server.core.provider;

public interface AccountProvider {

    public Boolean authenticate(String login, String password);

}
