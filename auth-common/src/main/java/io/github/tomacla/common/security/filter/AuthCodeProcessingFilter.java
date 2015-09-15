package io.github.tomacla.common.security.filter;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import io.github.tomacla.common.security.Headers;
import io.github.tomacla.common.security.authentication.TokenAuthentication;
import io.github.tomacla.common.service.TokenService;

public class AuthCodeProcessingFilter extends GenericFilterBean {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AuthCodeProcessingFilter.class);

    private TokenService tokenService;

    public AuthCodeProcessingFilter(TokenService tokenService) {
	this.tokenService = tokenService;
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
	    throws IOException, ServletException {

	HttpServletRequest request = (HttpServletRequest) req;

	if (SecurityContextHolder.getContext().getAuthentication() == null) {

	    LOGGER.debug("Search auth code in HTTP Headers");
	    String strAuthCode = request.getParameter(Headers.AUTH_CODE);

	    if (strAuthCode != null) {
		LOGGER.debug("Found auth code {}", strAuthCode);

		Optional<String> tokenFromAuthCode = tokenService.getTokenFromAuthCode(strAuthCode);
		if (tokenFromAuthCode.isPresent()) {
		    TokenAuthentication partialAuthenticationToken = new TokenAuthentication(tokenFromAuthCode.get());
		    SecurityContextHolder.getContext().setAuthentication(partialAuthenticationToken);
		} else {
		    LOGGER.info("Auth code {} could not be exchange with a token");
		}
	    }

	}

	chain.doFilter(request, res);
    }
}
