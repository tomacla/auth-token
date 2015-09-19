package io.github.tomacla.common.token;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import org.jose4j.lang.JoseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.github.tomacla.common.token.TokenDTO;
import io.github.tomacla.common.token.TokenManager;

public class TokenManagerTest {

    private TokenManager manager;
    
    @Before
    public void before() {
	this.manager = new TokenManager("thisisaverylongsecretandthisissocooltouseit", 5);
    }
    
    @Test
    public void getToken() throws JoseException, UnsupportedEncodingException {
	
	TokenDTO input = new TokenDTO();
	input.setEmail("john.doe@mail.org");
	input.setIssuer("iss");
	input.setSubject("sub");
	
	String token = this.manager.getToken(input);	
	
	Optional<TokenDTO> output = this.manager.readToken(token);
	
	Assert.assertTrue(output.isPresent());
	Assert.assertEquals(input.getEmail(), output.get().getEmail());
	Assert.assertEquals(input.getIssuer(), output.get().getIssuer());
	Assert.assertEquals(input.getSubject(), output.get().getSubject());
	
    }
    
    public void getTokenInvalidSecret() throws JoseException, UnsupportedEncodingException {
	
	TokenDTO input = new TokenDTO();
	input.setEmail("john.doe@mail.org");
	input.setIssuer("iss");
	input.setSubject("sub");
	
	String token = this.manager.getToken(input);	
	
	TokenManager falseManager = new TokenManager("aaathisisaverylongsecretandthisissocooltouseit", 5);
	
	falseManager.readToken(token);
	
    }
    
    public void isValid() throws JoseException, UnsupportedEncodingException {
	
	
	TokenDTO input = new TokenDTO();
	input.setEmail("john.doe@mail.org");
	input.setIssuer("iss");
	input.setSubject("sub");
	
	String token = this.manager.getToken(input);	
	
	Assert.assertFalse(this.manager.isValid( token));
	
    }
    
    public void isNotValid() throws JoseException, UnsupportedEncodingException {
	
	TokenDTO input = new TokenDTO();
	input.setEmail("john.doe@mail.org");
	input.setIssuer("iss");
	input.setSubject("sub");
	
	TokenManager shortManager = new TokenManager("thisisaverylongsecretandthisissocooltouseit", 0);
	
	String token = shortManager.getToken(input);	
	
	Assert.assertFalse(this.manager.isValid(token));
	
    }

}
