package io.github.tomacla.auth.server.core.service;

import java.util.Optional;

public interface AccountService {

    public Optional<String> authenticate(String login, String password);

    public void invalidateToken(String token);

    public Optional<String> verifyToken(String token);

}
