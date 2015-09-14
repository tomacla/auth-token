package io.github.tomacla.common.security.entrypoint;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

    protected static final Logger LOGGER = LoggerFactory.getLogger(UnauthorizedEntryPoint.class);
    protected static final String MESSAGE = "Unauthorized: an authentication token must be provided.";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
	    AuthenticationException authException) throws IOException, ServletException {
	LOGGER.debug("Entering UnauthorizedEntryPoint");
	response.sendError(HttpServletResponse.SC_UNAUTHORIZED, MESSAGE);
    }

}
