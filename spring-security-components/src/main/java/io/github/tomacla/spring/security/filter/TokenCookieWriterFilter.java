package io.github.tomacla.spring.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import io.github.tomacla.common.header.Headers;
import io.github.tomacla.spring.security.TokenAuthentication;

public class TokenCookieWriterFilter extends GenericFilterBean {

    protected static final Logger LOGGER = LoggerFactory.getLogger(TokenCookieWriterFilter.class);

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
	    throws IOException, ServletException {

	HttpServletRequest request = (HttpServletRequest) req;
	HttpServletResponse response = (HttpServletResponse) res;

	LOGGER.debug("Entering TokenCookieWriter");

	Integer xTokenCookieIndex = -1;
	Cookie[] cookies = request.getCookies();
	if (cookies != null) {
	    for (int i = 0; i < cookies.length; i++) {
		Cookie cookie = cookies[i];
		if (cookie.getName().equals(Headers.X_TOKEN)) {
		    LOGGER.debug("An existing cookie has been found");
		    xTokenCookieIndex = i;
		    break;
		}
	    }
	}

	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	if (authentication != null && TokenAuthentication.class.isAssignableFrom(authentication.getClass())) {

	    TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;

	    if (xTokenCookieIndex == -1 || !tokenAuthentication.getToken().equals(cookies[xTokenCookieIndex].getValue())) {

		Cookie cookie = new Cookie(Headers.X_TOKEN, tokenAuthentication.getToken());
		cookie.setMaxAge(31536000);
		cookie.setPath("/");

		LOGGER.debug("Write cookie with token {}", tokenAuthentication.getToken());

		response.addCookie(cookie);

	    } else {
		LOGGER.debug("Cookie already exists with same {} value", tokenAuthentication.getToken());
	    }

	} else if (authentication == null && xTokenCookieIndex != -1) {
	    Cookie cookie = new Cookie(Headers.X_TOKEN, "");
	    cookie.setMaxAge(0);
	    cookie.setPath("/");

	    LOGGER.debug("Deleting a cookie that is not valid anymore");

	    response.addCookie(cookie);

	}

	chain.doFilter(request, response);

    }

}