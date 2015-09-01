package com.thomasclarisse.auth.server.service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thomas on 01/09/2015.
 */
public class AuthenticationService {

    private Map<String, String> tokens;
    private SecureRandom random;

    public AuthenticationService() {
        this.tokens = new HashMap<>();
        this.random =  new SecureRandom();
    }

    public String generateToken(String email) {
        try {
            String token = new BigInteger(130, random).toString(32);
            this.tokens.put(token, email);
            return token;
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void invalidateToken(String token)  {
        try {
            this.tokens.remove(token);
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String verifyToken(String token)  {
        try {
            if(token == null || !this.tokens.keySet().contains(token)) {
                throw new RuntimeException();
            }
            return this.tokens.get(token);
        }
        catch (Exception e) {
            this.invalidateToken(token);
            throw new RuntimeException(e);
        }
    }

}
