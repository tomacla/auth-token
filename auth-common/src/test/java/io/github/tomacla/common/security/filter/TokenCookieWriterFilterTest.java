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
	Mockito.when(request.getCookies()).thenReturn(new Cookie[] {});
	TokenAuthentication auth = new TokenAuthentication("token");
	SecurityContextHolder.getContext().setAuthentication(auth);
	writer.doFilter(request, response, chain);
	Mockito.verify(response).addCookie(Mockito.any(Cookie.class));
	Mockito.verify(chain).doFilter(request, response);
    }
    
    @Test
    public void writeHeadersAlreadyExists() throws IOException, ServletException {
	Cookie existing = new Cookie("X-Token", "token");
	Mockito.when(request.getCookies()).thenReturn(new Cookie[] {existing});
	TokenAuthentication auth = new TokenAuthentication("token");
	SecurityContextHolder.getContext().setAuthentication(auth);
	writer.doFilter(request, response, chain);
	Mockito.verify(response, Mockito.never()).addCookie(Mockito.any(Cookie.class));
	Mockito.verify(chain).doFilter(request, response);
    }
    
    @Test
    public void writeHeadersAlreadyExistsButDifferent() throws IOException, ServletException {
	Cookie existing = new Cookie("X-Token", "token_old");
	Mockito.when(request.getCookies()).thenReturn(new Cookie[] {existing});
	TokenAuthentication auth = new TokenAuthentication("token");
	SecurityContextHolder.getContext().setAuthentication(auth);
	writer.doFilter(request, response, chain);
	Mockito.verify(response).addCookie(Mockito.any(Cookie.class));
	Mockito.verify(chain).doFilter(request, response);
    }
    
    @Test
    public void writeHeadersOtherAuthentication() throws IOException, ServletException {
	Mockito.when(request.getCookies()).thenReturn(new Cookie[] {});
	UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("test", "test");
	SecurityContextHolder.getContext().setAuthentication(auth);
	writer.doFilter(request, response, chain);
	Mockito.verify(response, Mockito.never()).addCookie(Mockito.any(Cookie.class));
	Mockito.verify(chain).doFilter(request, response);
    }
    
    @Test
    public void writeHeadersNullAuthentication() throws IOException, ServletException {
	Mockito.when(request.getCookies()).thenReturn(new Cookie[] {});
	writer.doFilter(request, response, chain);
	Mockito.verify(response, Mockito.never()).addCookie(Mockito.any(Cookie.class));
	Mockito.verify(chain).doFilter(request, response);
    }
    
    @Test
    public void writeHeadersNullAuthenticationButCookieExists() throws IOException, ServletException {
	Cookie existing = new Cookie("X-Token", "token_old");
	Mockito.when(request.getCookies()).thenReturn(new Cookie[] {existing});
	writer.doFilter(request, response, chain);
	Mockito.verify(response).addCookie(Mockito.any(Cookie.class));
	Mockito.verify(chain).doFilter(request, response);
    }
    
    
    
}