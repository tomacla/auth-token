package com.thomasclarisse.auth.server.service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Named;

@Named
public class AuthenticationService {

	private Map<String, String> accounts;
	private Map<String, String> authCodes;
    private Map<String, String> tokens;
    private SecureRandom random;

    public AuthenticationService() {
        this.tokens = new HashMap<>();
        this.authCodes = new HashMap<>();
        this.accounts = new HashMap<>();
        this.random =  new SecureRandom();
        this.initAccounts();
    }
    
    private void initAccounts() {
    	this.accounts.put("tomacla", "tomaclapwd");
    }
    
    public Optional<String> authenticate(String login, String password) {
    	if(this.accounts.containsKey(login) && this.accounts.get(login).equals(password)) {
    		String uniqueCode = UUID.randomUUID().toString();
    		this.authCodes.put(uniqueCode, login);
    		return Optional.of(uniqueCode);
    	}
    	return Optional.empty();
    }

    public Optional<String> getTokenFromAuthCode(String authCode) {
    	if(this.authCodes.containsKey(authCode)) {
    		String login = this.authCodes.get(authCode);
    		String newToken = this.generateToken(login);
    		return Optional.of(newToken);
    	}
    	return Optional.empty();
    }
    
    private String generateToken(String login) {
        String token = new BigInteger(130, random).toString(32);
        this.tokens.put(token, login);
        return token;
    }

    public void invalidateToken(String token)  {
        this.tokens.remove(token);
    }

    public Optional<String> verifyToken(String token)  {
        if(this.tokens.containsKey(token)) {
        	return Optional.of(this.tokens.get(token));
        }
        return Optional.empty();
    }

}
