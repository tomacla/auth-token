package io.github.tomacla.common.service;

import java.util.Optional;

public interface TokenService {

    public Boolean verify(String token);
    public Optional<String> getTokenFromAuthCode(String authCode);

}