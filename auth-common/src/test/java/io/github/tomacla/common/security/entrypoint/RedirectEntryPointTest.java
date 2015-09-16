package io.github.tomacla.common.security.entrypoint;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.AuthenticationException;

import io.github.tomacla.common.security.entrypoint.RedirectEntryPoint;

public class RedirectEntryPointTest {

    private RedirectEntryPoint entryPoint;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private AuthenticationException exception;

    @Before
    public void before() {
	entryPoint = new RedirectEntryPoint("http://targetpath/%s");
	request = Mockito.mock(HttpServletRequest.class);
	response = Mockito.mock(HttpServletResponse.class);
	exception = Mockito.mock(AuthenticationException.class);
    }

    @Test
    public void commence() throws IOException, ServletException {
	Mockito.when(request.getRequestURL()).thenReturn(new StringBuffer("/url"));
	entryPoint.commence(request, response, exception);
	Mockito.verify(response).sendRedirect("http://targetpath/%2Furl");
    }

    @Test
    public void commenceWithParams() throws IOException, ServletException {
	Mockito.when(request.getRequestURL()).thenReturn(new StringBuffer("/url"));
	Mockito.when(request.getQueryString()).thenReturn("foo=bar&hello=world");
	entryPoint.commence(request, response, exception);
	Mockito.verify(response)
		.sendRedirect("http://targetpath/%2Furl%3Ffoo%3Dbar%26hello%3Dworld");
    }

}
