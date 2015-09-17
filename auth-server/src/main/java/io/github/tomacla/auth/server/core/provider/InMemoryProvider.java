package io.github.tomacla.auth.server.core.provider;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class InMemoryProvider implements AccountProvider {

    protected static final Logger LOGGER = LoggerFactory.getLogger(InMemoryProvider.class);

    private List<InMemoryAccount> accounts;

    public InMemoryProvider() {
	try {
	    ObjectMapper om = new ObjectMapper();
	    InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("users.json");
	    accounts = om.readValue(resourceAsStream, new TypeReference<List<InMemoryAccount>>() {
	    });
	} catch (IOException e) {
	    LOGGER.error("Unable to init InMemoryProvider", e);
	    throw new RuntimeException("Unable to init InMemoryProvider", e);
	}
    }

    @Override
    public Boolean authenticate(String login, String password) {
	if(login == null || password == null) {
	    return false;
	}
	for(InMemoryAccount account : accounts) {
	    if(account.login.equals(login) && account.password.equals(password)) {
		return true;
	    }
	}
	return false;
    }
    
    public static class InMemoryAccount {
	
	public String login;
	public String password;
	
    }

}
