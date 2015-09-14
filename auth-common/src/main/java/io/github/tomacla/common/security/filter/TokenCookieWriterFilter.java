package io.github.tomacla.common.security.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.web.filter.GenericFilterBean;

import io.github.tomacla.common.security.Headers;
import io.github.tomacla.common.security.authentication.TokenAuthentication;

public class TokenCookieWriterFilter extends GenericFilterBean {

    protected static final Logger LOGGER = LoggerFactory.getLogger(TokenCookieWriterFilter.class);
    public static final Class<? extends Filter> AFTER_POSITION = SwitchUserFilter.class;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
	    throws IOException, ServletException {

	HttpServletResponse response = (HttpServletResponse) res;

	LOGGER.debug("Entering TokenCookieWriter");

	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	if (authentication != null && TokenAuthentication.class.isAssignableFrom(authentication.getClass())) {

	    TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;

	    Cookie cookie = new Cookie(Headers.X_TOKEN, tokenAuthentication.getToken());
	    cookie.setMaxAge(31536000);
	    cookie.setPath("/");

	    LOGGER.debug("Write cookie with token {}", tokenAuthentication.getToken());

	    response.addCookie(cookie);

	}

	chain.doFilter(req, response);

    }

}