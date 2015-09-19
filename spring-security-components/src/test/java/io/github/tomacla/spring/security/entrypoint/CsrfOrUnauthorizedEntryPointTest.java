package io.github.tomacla.spring.security.entrypoint;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.AuthenticationException;

public class CsrfOrUnauthorizedEntryPointTest {

    private CsrfOrUnauthorizedEntryPoint entryPoint;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private AuthenticationException exception;

    @Before
    public void before() {
	entryPoint = new CsrfOrUnauthorizedEntryPoint("*", Arrays.asList("GET"), Arrays.asList("X-Token"));
	request = Mockito.mock(HttpServletRequest.class);
	response = Mockito.mock(HttpServletResponse.class);
	exception = Mockito.mock(AuthenticationException.class);
    }

    @Test
    public void commence() throws IOException, ServletException {
	Mockito.when(request.getMethod()).thenReturn("GET");
	entryPoint.commence(request, response, exception);
	Mockito.verify(response).sendError(Mockito.eq(401), Mockito.anyString());
    }
    
    @Test
    public void commenceWithOptions() throws IOException, ServletException {
	Mockito.when(request.getMethod()).thenReturn("OPTIONS");
	entryPoint.commence(request, response, exception);
	Mockito.verify(response, Mockito.times(3)).addHeader(Mockito.anyString(), Mockito.anyString());
	Mockito.verify(response).setStatus(Mockito.eq(200));
    }

    
}
