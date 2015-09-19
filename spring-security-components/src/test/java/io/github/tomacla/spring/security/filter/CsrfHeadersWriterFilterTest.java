package io.github.tomacla.spring.security.filter;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.context.SecurityContextHolder;

public class CsrfHeadersWriterFilterTest {

    private CsrfHeadersWriterFilter writer;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain chain;
    
    @Before
    public void before() {
	writer = new CsrfHeadersWriterFilter("*", Arrays.asList("GET"), Arrays.asList("X-Token"));
	request = Mockito.mock(HttpServletRequest.class);
	response = Mockito.mock(HttpServletResponse.class);
	chain = Mockito.mock(FilterChain.class);
	SecurityContextHolder.clearContext();
    }
    
    @Test
    public void writeHeaders() throws IOException, ServletException {
	writer.doFilter(request, response, chain);
	Mockito.verify(response, Mockito.times(3)).addHeader(Mockito.anyString(), Mockito.anyString());
	Mockito.verify(chain).doFilter(request, response);
    }
    
}
