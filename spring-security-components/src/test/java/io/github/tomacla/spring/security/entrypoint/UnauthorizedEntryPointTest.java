package io.github.tomacla.spring.security.entrypoint;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.AuthenticationException;

import io.github.tomacla.spring.security.entrypoint.UnauthorizedEntryPoint;

public class UnauthorizedEntryPointTest {

    private UnauthorizedEntryPoint entryPoint;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private AuthenticationException exception;

    @Before
    public void before() {
	entryPoint = new UnauthorizedEntryPoint();
	request = Mockito.mock(HttpServletRequest.class);
	response = Mockito.mock(HttpServletResponse.class);
	exception = Mockito.mock(AuthenticationException.class);
    }

    @Test
    public void commence() throws IOException, ServletException {
	entryPoint.commence(request, response, exception);
	Mockito.verify(response).sendError(Mockito.eq(401), Mockito.anyString());
    }

}
