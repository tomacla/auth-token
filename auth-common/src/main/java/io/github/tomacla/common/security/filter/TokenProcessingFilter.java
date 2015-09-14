package io.github.tomacla.common.security.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.web.filter.GenericFilterBean;

import io.github.tomacla.common.security.Headers;
import io.github.tomacla.common.security.authentication.TokenAuthentication;

public class TokenProcessingFilter extends GenericFilterBean {

    protected static final Logger LOGGER = LoggerFactory.getLogger(TokenProcessingFilter.class);
    public static final Class<? extends Filter> AFTER_POSITION = AbstractPreAuthenticatedProcessingFilter.class;

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
	    throws IOException, ServletException {

	HttpServletRequest request = (HttpServletRequest) req;

	LOGGER.debug("Search token in HTTP Headers");
	String strToken = request.getHeader(Headers.X_TOKEN);
	if (strToken == null) {
	    LOGGER.debug("Search token in Query Parameters");
	    strToken = request.getParameter(Headers.X_TOKEN);
	}
	if (strToken == null) {
	    LOGGER.debug("Search token in Cookies");
	    Cookie[] cookies = request.getCookies();
	    if (cookies != null) {
		for (int i = 0; i < cookies.length; i++) {
		    Cookie cookie = cookies[i];
		    if (cookie.getName().equals(Headers.X_TOKEN)) {
			strToken = cookie.getValue();
			break;
		    }
		}
	    }
	}

	if (strToken != null) {
	    LOGGER.debug("Found token {}", strToken);
	    TokenAuthentication partialAuthenticationToken = new TokenAuthentication(strToken);
	    SecurityContextHolder.getContext().setAuthentication(partialAuthenticationToken);
	}

	chain.doFilter(request, res);
    }
}
