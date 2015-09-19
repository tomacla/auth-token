package io.github.tomacla.spring.security.entrypoint;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;

import io.github.tomacla.common.csrf.CsrfUtils;

public class CsrfOrUnauthorizedEntryPoint extends UnauthorizedEntryPoint {

    protected static final Logger LOGGER = LoggerFactory.getLogger(CsrfOrUnauthorizedEntryPoint.class);

    private String allowOrigin;
    private List<String> allowMethods;
    private List<String> allowHeaders;
    
    public CsrfOrUnauthorizedEntryPoint(String allowOrigin, List<String> allowMethods, List<String> allowHeaders) {
	super();
	this.allowOrigin = allowOrigin;
	this.allowMethods = allowMethods;
	this.allowHeaders = allowHeaders;
    }


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
	    AuthenticationException authException) throws IOException, ServletException {
	
	LOGGER.debug("Entering CsrfHeadersWriterFilter");

	if (request.getMethod().equals("OPTIONS")) {

	    LOGGER.debug("Adding CSRF Headers");

	    CsrfUtils.writeCsrfHeaders(response, allowOrigin, allowMethods, allowHeaders);

	    response.setStatus(HttpServletResponse.SC_OK);

	} else {
	    super.commence(request, response, authException);
	}
	
    }

}