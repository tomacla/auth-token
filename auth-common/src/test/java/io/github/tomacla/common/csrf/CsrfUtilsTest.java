package io.github.tomacla.common.csrf;

import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.MultivaluedHashMap;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class CsrfUtilsTest {

    @Test
    public void writeCsrfHeaders() {
	HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
	CsrfUtils.writeCsrfHeaders(response, null, null, null);
	Mockito.verifyNoMoreInteractions(response);
    }

    @Test
    public void writeCsrfHeadersOrigin() {
	HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
	CsrfUtils.writeCsrfHeaders(response, "*", null, null);
	Mockito.verify(response, Mockito.times(1)).addHeader("Access-Control-Allow-Origin", "*");
	Mockito.verifyNoMoreInteractions(response);
    }

    @Test
    public void writeCsrfHeadersMethodsEmpty() {
	HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
	CsrfUtils.writeCsrfHeaders(response, null, new ArrayList<String>(), null);
	Mockito.verifyNoMoreInteractions(response);
    }

    @Test
    public void writeCsrfHeadersMethods() {
	HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
	CsrfUtils.writeCsrfHeaders(response, null, Arrays.asList("GET", "POST"), null);
	Mockito.verify(response, Mockito.times(1)).addHeader("Access-Control-Allow-Methods", "GET, POST");
	Mockito.verifyNoMoreInteractions(response);
    }

    @Test
    public void writeCsrfHeadersHeadersEmpty() {
	HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
	CsrfUtils.writeCsrfHeaders(response, null, null, new ArrayList<String>());
	Mockito.verifyNoMoreInteractions(response);
    }

    @Test
    public void writeCsrfHeadersHeaders() {
	HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
	CsrfUtils.writeCsrfHeaders(response, null, null, Arrays.asList("X-H", "X-V"));
	Mockito.verify(response, Mockito.times(1)).addHeader("Access-Control-Allow-Headers", "X-H, X-V");
	Mockito.verifyNoMoreInteractions(response);
    }
    
    @Test
    public void writeCsrfContainerHeaders() {
	ContainerResponseContext response = Mockito.mock(ContainerResponseContext.class);
	CsrfUtils.writeCsrfHeaders(response, null, null, null);
	Mockito.verifyNoMoreInteractions(response);
    }

    @Test
    public void writeCsrfContainerHeadersOrigin() {
	MultivaluedHashMap<String, Object> headers = new MultivaluedHashMap<>();
	ContainerResponseContext response = Mockito.mock(ContainerResponseContext.class);
	Mockito.when(response.getHeaders()).thenReturn(headers);
	CsrfUtils.writeCsrfHeaders(response, "*", null, null);
	Assert.assertEquals(headers.getFirst("Access-Control-Allow-Origin"), "*");
    }

    @Test
    public void writeCsrfContainerHeadersMethodsEmpty() {
	MultivaluedHashMap<String, Object> headers = new MultivaluedHashMap<>();
	ContainerResponseContext response = Mockito.mock(ContainerResponseContext.class);
	Mockito.when(response.getHeaders()).thenReturn(headers);
	CsrfUtils.writeCsrfHeaders(response, null, new ArrayList<String>(), null);
	Assert.assertNull(headers.getFirst("Access-Control-Allow-Methods"));
    }

    @Test
    public void writeCsrfContainerHeadersMethods() {
	MultivaluedHashMap<String, Object> headers = new MultivaluedHashMap<>();
	ContainerResponseContext response = Mockito.mock(ContainerResponseContext.class);
	Mockito.when(response.getHeaders()).thenReturn(headers);
	CsrfUtils.writeCsrfHeaders(response, null, Arrays.asList("GET", "POST"), null);
	Assert.assertEquals(headers.getFirst("Access-Control-Allow-Methods"), "GET, POST");
    }

    @Test
    public void writeCsrfContainerHeadersHeadersEmpty() {
	MultivaluedHashMap<String, Object> headers = new MultivaluedHashMap<>();
	ContainerResponseContext response = Mockito.mock(ContainerResponseContext.class);
	Mockito.when(response.getHeaders()).thenReturn(headers);
	CsrfUtils.writeCsrfHeaders(response, null, null, new ArrayList<String>());
	Assert.assertNull(headers.getFirst("Access-Control-Allow-Headers"));
    }

    @Test
    public void writeCsrfContainerHeadersHeaders() {
	MultivaluedHashMap<String, Object> headers = new MultivaluedHashMap<>();
	ContainerResponseContext response = Mockito.mock(ContainerResponseContext.class);
	Mockito.when(response.getHeaders()).thenReturn(headers);
	CsrfUtils.writeCsrfHeaders(response, null, null, Arrays.asList("X-H", "X-V"));
	Assert.assertEquals(headers.getFirst("Access-Control-Allow-Headers"), "X-H, X-V");
    }

}
