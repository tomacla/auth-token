package io.github.tomacla.common.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import io.github.tomacla.common.security.Headers;
import io.github.tomacla.common.security.authentication.TokenAuthentication;

public class TokenParamWriterFilter extends GenericFilterBean {

    protected static final Logger LOGGER = LoggerFactory.getLogger(TokenParamWriterFilter.class);

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
	    throws IOException, ServletException {

	HttpServletResponse response = (HttpServletResponse) res;

	LOGGER.debug("Entering TokenParamWriter");

	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	if (authentication != null && TokenAuthentication.class.isAssignableFrom(authentication.getClass())) {

	    TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;

	    LOGGER.debug("Writing token {}", tokenAuthentication.getToken());

	    response.addHeader(Headers.X_TOKEN, tokenAuthentication.getToken());

	}

	chain.doFilter(req, response);

    }

}
