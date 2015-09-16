package io.github.tomacla.auth.server.core.service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.tomacla.common.security.token.TokenDTO;
import io.github.tomacla.common.security.token.TokenManager;

@Named
public class DefaultAccountService implements AccountService {

    // TODO introduce auth providers ALWAYS_TRUE, LDAP, JDBC, IN_MEMORY

    protected static final Logger LOGGER = LoggerFactory.getLogger(DefaultAccountService.class);
    protected static final String ISSUER = "AUTH_SERVER";
    
    
    
    private Map<String, String> accounts;
    private Map<String, String> authCodes;
    private SecureRandom random;
    private TokenManager tokenManager;

    public DefaultAccountService(Integer tokenValidityInDays, String secret) {
	this.tokenManager = new TokenManager(secret, tokenValidityInDays);
	this.authCodes = new HashMap<>();
	this.accounts = new HashMap<>();
	this.random = new SecureRandom();
	this.initAccounts();
    }

    private void initAccounts() {
	// TODO replace this with providers
	this.accounts.put("tomacla", "tomaclapwd");
    }

    public Optional<String> authenticate(String login, String password) {
	LOGGER.debug("Authentication of {}", login);
	// TODO request providers instead of this
	if (this.accounts.containsKey(login) && this.accounts.get(login).equals(password)) {
	    return Optional.of(this.generateToken(login));
	}
	return Optional.empty();
    }

    public String getAuthCodeForToken(String token) {
	String authCode = generateAuthCode();
	this.authCodes.put(authCode, token);
	return authCode;
    }

    public Optional<String> getTokenFromAuthCode(String authCode) {
	if (this.authCodes.containsKey(authCode)) {
	    return Optional.of(this.authCodes.get(authCode));
	}
	return Optional.empty();
    }

    private String generateToken(String login) {
	TokenDTO tokenDTO = new TokenDTO();
	tokenDTO.setIssuer(ISSUER);
	tokenDTO.setEmail(login);
	tokenDTO.setSubject("Authentication token");
	return this.tokenManager.getToken(tokenDTO);
    }

    private String generateAuthCode() {
	return new BigInteger(130, random).toString(32);
    }

    public Boolean verifyToken(String token) {
	LOGGER.debug("Verifying {}", token);
	return this.tokenManager.isValid(token);
    }

}
