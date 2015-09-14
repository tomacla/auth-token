package io.github.tomacla.common.security.provider;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import io.github.tomacla.common.security.authentication.TokenAuthentication;
import io.github.tomacla.common.service.TokenService;

public class TokenAuthenticationProvider implements AuthenticationProvider {

    protected static final Logger LOGGER = LoggerFactory.getLogger(TokenAuthenticationProvider.class);
    
    private TokenService authService;

    public TokenAuthenticationProvider(TokenService authService) {
	this.authService = authService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
	if (this.supports(authentication.getClass())) {
	    LOGGER.debug("Handling token authentication");
	    return handleToken((TokenAuthentication) authentication);
	}
	return null;
    }

    private Authentication handleToken(TokenAuthentication partial) {
	if (partial.getToken() == null) {
	    LOGGER.debug("Token is null");
	    throw new BadCredentialsException("No token found in request.");
	}

	Boolean verified = authService.verify(partial.getToken());
	if (!verified) {
	    LOGGER.debug("Token is not valid");
	    throw new BadCredentialsException("Invalid token is provided");
	}

	TokenAuthentication full = new TokenAuthentication(partial.getToken(),
		Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));

	LOGGER.debug("Token is authenticated");
	return full;
    }

    @Override
    public boolean supports(Class<?> authenticationClass) {
	return TokenAuthentication.class.isAssignableFrom(authenticationClass);
    }

}
