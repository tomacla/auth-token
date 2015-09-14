package io.github.tomacla.common.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import io.github.tomacla.common.security.authentication.TokenAuthentication;

public class TokenCookieWriterFilterTest {

    private TokenCookieWriterFilter writer;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain chain;
    
    @Before
    public void before() {
	writer = new TokenCookieWriterFilter();
	request = Mockito.mock(HttpServletRequest.class);
	response = Mockito.mock(HttpServletResponse.class);
	chain = Mockito.mock(FilterChain.class);
	SecurityContextHolder.clearContext();
    }
    
    @Test
    public void writeHeaders() throws IOException, ServletException {
	TokenAuthentication auth = new TokenAuthentication("token");
	SecurityContextHolder.getContext().setAuthentication(auth);
	Mockito.when(request.getRequestURL()).thenReturn(new StringBuffer("http://www.domain.com/page"));
	writer.doFilter(request, response, chain);
	Mockito.verify(response).addCookie(Mockito.any(Cookie.class));
	Mockito.verify(chain).doFilter(request, response);
    }
    
    @Test
    public void writeHeadersOtherAuthentication() throws IOException, ServletException {
	UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("test", "test");
	SecurityContextHolder.getContext().setAuthentication(auth);
	writer.doFilter(request, response, chain);
	Mockito.verify(response, Mockito.never()).addCookie(Mockito.any(Cookie.class));
	Mockito.verify(chain).doFilter(request, response);
    }
    
    
}