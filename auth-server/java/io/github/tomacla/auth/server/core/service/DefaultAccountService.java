package io.github.tomacla.auth.server.core.service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class DefaultAccountService {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DefaultAccountService.class);
    
    private Map<String, String> accounts;
    private Map<String, String> tokens;
    private Map<String, String> authCodes;
    private SecureRandom random;

    public DefaultAccountService() {
	this.tokens = new HashMap<>();
	this.authCodes = new HashMap<>();
	this.accounts = new HashMap<>();
	this.random = new SecureRandom();
	this.initAccounts();
    }

    private void initAccounts() {
	this.accounts.put("tomacla", "tomaclapwd");
    }

    public Optional<String> authenticate(String login, String password) {
	LOGGER.debug("Authentication of {}", login);
	if (this.accounts.containsKey(login) && this.accounts.get(login).equals(password)) {
	    String token = this.generateToken(login);
	    this.tokens.put(token, login);
	    return Optional.of(token);
	}
	return Optional.empty();
    }
    
    public String getAuthCodeForToken(String token) {
	String authCode = UUID.randomUUID().toString();
	this.authCodes.put(authCode, token);
	return authCode;
    }
    
    public Optional<String> getTokenFromAuthCode(String authCode) {
	if(this.authCodes.containsKey(authCode)) {
	    return Optional.of(this.authCodes.get(authCode));
	}
	return Optional.empty();
    }

    private String generateToken(String login) {
	return new BigInteger(130, random).toString(32);
    }

    public void invalidateToken(String token) {
	this.tokens.remove(token);
    }

    public Optional<String> verifyToken(String token) {
	LOGGER.debug("Verifying {}", token);
	if (this.tokens.containsKey(token)) {
	    return Optional.of(this.tokens.get(token));
	}
	return Optional.empty();
    }

}
