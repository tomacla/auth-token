package io.github.tomacla.auth.server.api.csrf;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import io.github.tomacla.common.csrf.CsrfUtils;

@Provider
public class CsrfFilter implements ContainerResponseFilter {

   private CsrfConfig csrfConfig;
    
    @Inject
    public CsrfFilter(CsrfConfig csrfConfig) {
	this.csrfConfig = csrfConfig;
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
	    throws IOException {
	
	CsrfUtils.writeCsrfHeaders(responseContext, csrfConfig.allowOrigin, csrfConfig.allowMethods, csrfConfig.allowHeaders);
	
	if(requestContext.getMethod().equals("OPTIONS")) {
	    responseContext.setStatus(Response.Status.OK.getStatusCode());
	}
	
    }
    
}
