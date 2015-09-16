package io.github.tomacla.common.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.context.SecurityContextHolder;

import io.github.tomacla.common.security.authentication.TokenAuthentication;

public class TokenProcessingFilterTest {

    private TokenProcessingFilter filter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain chain;

    @Before
    public void before() {
	filter = new TokenProcessingFilter(TokenProcessing.BOTH);
	request = Mockito.mock(HttpServletRequest.class);
	response = Mockito.mock(HttpServletResponse.class);
	chain = Mockito.mock(FilterChain.class);
	SecurityContextHolder.clearContext();
    }

    @Test
    public void doFilter() throws IOException, ServletException {
	filter.doFilter(request, response, chain);
	Mockito.verify(chain).doFilter(request, response);
	Assert.assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    
    @Test
    public void doFilterExistingAuthentication() throws IOException, ServletException {
	SecurityContextHolder.getContext().setAuthentication(new TokenAuthentication("foo"));
	filter.doFilter(request, response, chain);
	Mockito.verify(request, Mockito.never()).getHeader(Mockito.anyString());
	Mockito.verify(chain).doFilter(request, response);
	Assert.assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void doFilterWithTokenAsHeader() throws IOException, ServletException {
	Mockito.when(request.getHeader("X-Token")).thenReturn("token");
	filter.doFilter(request, response, chain);
	Mockito.verify(chain).doFilter(request, response);
	TokenAuthentication auth = (TokenAuthentication) SecurityContextHolder.getContext().getAuthentication();
	Assert.assertEquals("token", auth.getToken());
    }
    
    @Test
    public void doFilterWithTokenAsHeaderButNotSupported() throws IOException, ServletException {
	filter = new TokenProcessingFilter(TokenProcessing.COOKIE);
	Mockito.when(request.getHeader("X-Token")).thenReturn("token");
	filter.doFilter(request, response, chain);
	Mockito.verify(chain).doFilter(request, response);
	Assert.assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void doFilterWithTokenAsCookie() throws IOException, ServletException {
	Cookie cookie = new Cookie("X-Token", "token");
	Cookie[] cookies = new Cookie[]{cookie};
	Mockito.when(request.getCookies()).thenReturn(cookies);
	filter.doFilter(request, response, chain);
	Mockito.verify(chain).doFilter(request, response);
	TokenAuthentication auth = (TokenAuthentication) SecurityContextHolder.getContext().getAuthentication();
	Assert.assertEquals("token", auth.getToken());
    }
    
    @Test
    public void doFilterWithTokenAsCookieButNotSupported() throws IOException, ServletException {
	filter = new TokenProcessingFilter(TokenProcessing.HTTP_PARAM);
	Cookie cookie = new Cookie("X-Token", "token");
	Cookie[] cookies = new Cookie[]{cookie};
	Mockito.when(request.getCookies()).thenReturn(cookies);
	filter.doFilter(request, response, chain);
	Mockito.verify(chain).doFilter(request, response);
	Assert.assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    
    @Test
    public void doFilterWithCookiesButNotToken() throws IOException, ServletException {
	Cookie cookie = new Cookie("Cook", "'ngo");
	Cookie[] cookies = new Cookie[]{cookie};
	Mockito.when(request.getCookies()).thenReturn(cookies);
	filter.doFilter(request, response, chain);
	Mockito.verify(chain).doFilter(request, response);
	Assert.assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    
    @Test
    public void doFilterWithNullCookies() throws IOException, ServletException {
	Mockito.when(request.getCookies()).thenReturn(null);
	filter.doFilter(request, response, chain);
	Mockito.verify(chain).doFilter(request, response);
	Assert.assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

}
