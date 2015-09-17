package io.github.tomacla.common.security.provider;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import io.github.tomacla.common.security.authentication.TokenAuthentication;
import io.github.tomacla.common.security.token.TokenDTO;
import io.github.tomacla.common.service.TokenService;

public class TokenAuthenticationProvider implements AuthenticationProvider {

    protected static final Logger LOGGER = LoggerFactory.getLogger(TokenAuthenticationProvider.class);
    
    private TokenService authService;
    private UserDetailsService userDetailsService;

    public TokenAuthenticationProvider(TokenService authService) {
	this.authService = authService;
	this.userDetailsService = authService;
    }
    
    public TokenAuthenticationProvider(TokenService authService, UserDetailsService userDetailsService) {
	this.authService = authService;
	this.userDetailsService = userDetailsService;
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

	Optional<TokenDTO> tokenData = authService.verify(partial.getToken());
	if (!tokenData.isPresent()) {
	    LOGGER.debug("Token is not valid");
	    throw new BadCredentialsException("Invalid token is provided");
	}

	UserDetails userDetails = userDetailsService.loadUserByUsername(tokenData.get().getEmail());
	TokenAuthentication full = new TokenAuthentication(partial.getToken(), userDetails);
	full.setAuthenticated(true);
	
	LOGGER.debug("Token is authenticated");
	return full;
    }

    @Override
    public boolean supports(Class<?> authenticationClass) {
	return TokenAuthentication.class.isAssignableFrom(authenticationClass);
    }

}
