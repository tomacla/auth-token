package io.github.tomacla.auth.server.core.service;

import java.util.Optional;

public interface AccountService {

    public Optional<String> authenticate(String login, String password);

    public Boolean verifyToken(String token);
    
    public String getAuthCodeForToken(String token);
    
    public Optional<String> getTokenFromAuthCode(String authCode);

}
