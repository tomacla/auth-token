package io.github.tomacla.spring.security.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import io.github.tomacla.common.csrf.CsrfUtils;

public class CsrfHeadersWriterFilter extends GenericFilterBean {

    protected static final Logger LOGGER = LoggerFactory.getLogger(CsrfHeadersWriterFilter.class);

    private String allowOrigin;
    private List<String> allowMethods;
    private List<String> allowHeaders;

    public CsrfHeadersWriterFilter(String allowOrigin, List<String> allowMethods, List<String> allowHeaders) {
	super();
	this.allowOrigin = allowOrigin;
	this.allowMethods = allowMethods;
	this.allowHeaders = allowHeaders;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
	    throws IOException, ServletException {

	HttpServletRequest request = (HttpServletRequest) req;
	HttpServletResponse response = (HttpServletResponse) res;

	LOGGER.debug("Entering CsrfHeadersWriterFilter");

	CsrfUtils.writeCsrfHeaders(response, allowOrigin, allowMethods, allowHeaders);

	chain.doFilter(request, response);

    }

}