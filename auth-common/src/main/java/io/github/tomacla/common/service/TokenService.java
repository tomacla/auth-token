package io.github.tomacla.common.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import io.github.tomacla.common.security.token.TokenDTO;

public interface TokenService extends UserDetailsService {

    public Optional<TokenDTO> verify(String token);
    public Optional<String> getTokenFromAuthCode(String authCode);

}