package io.github.tomacla.common.security.entrypoint;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class RedirectEntryPoint implements AuthenticationEntryPoint {

    protected static final Logger LOGGER = LoggerFactory.getLogger(RedirectEntryPoint.class);
    
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
	    AuthenticationException authException) throws IOException, ServletException {

	LOGGER.debug("Entering RedirectEntryPoint");
	
	StringBuffer requestURL = request.getRequestURL();
	String queryString = request.getQueryString();

	String full = requestURL.toString();
	if (queryString != null) {
	    full = requestURL.append('?').append(queryString).toString();
	}

	// TODO put that in a variable
	String redirectUrl = "http://localhost:8080/auth-server/login?redirect_to=" + URLEncoder.encode(full, "UTF-8");
	
	LOGGER.debug("Redirect to {}", redirectUrl);
	
	response.sendRedirect(redirectUrl);
    }

}