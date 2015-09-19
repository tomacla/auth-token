package io.github.tomacla.spring.security.filter;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.context.SecurityContextHolder;

import io.github.tomacla.spring.security.TokenAuthentication;
import io.github.tomacla.spring.security.filter.AuthCodeProcessingFilter;
import io.github.tomacla.spring.security.service.TokenService;

public class AuthCodeProcessingFilterTest {

    private TokenService tokenService;
    private AuthCodeProcessingFilter filter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain chain;

    @Before
    public void before() {
	tokenService = Mockito.mock(TokenService.class);
	filter = new AuthCodeProcessingFilter(tokenService);
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
	Mockito.verify(request, Mockito.never()).getParameter(Mockito.anyString());
	Mockito.verify(chain).doFilter(request, response);
	Assert.assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void doFilterWithCodeAsParam() throws IOException, ServletException {
	Mockito.when(request.getParameter("auth_code")).thenReturn("code");
	Mockito.when(tokenService.getTokenFromAuthCode("code")).thenReturn(Optional.of("token"));
	filter.doFilter(request, response, chain);
	Mockito.verify(chain).doFilter(request, response);
	TokenAuthentication auth = (TokenAuthentication) SecurityContextHolder.getContext().getAuthentication();
	Assert.assertEquals("token", auth.getToken());
    }
    
    @Test
    public void doFilterWithUnknownCodeAsParam() throws IOException, ServletException {
	Mockito.when(request.getParameter("auth_code")).thenReturn("code");
	Mockito.when(tokenService.getTokenFromAuthCode("code")).thenReturn(Optional.empty());
	filter.doFilter(request, response, chain);
	Mockito.verify(chain).doFilter(request, response);
	Assert.assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    

}
