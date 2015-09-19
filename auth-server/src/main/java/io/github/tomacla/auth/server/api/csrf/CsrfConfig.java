package io.github.tomacla.auth.server.api.csrf;

import java.util.List;

public class CsrfConfig {

    public String allowOrigin;
    public List<String> allowMethods;
    public List<String> allowHeaders;
    
    public CsrfConfig(String allowOrigin, List<String> allowMethods, List<String> allowHeaders) {
	this.allowOrigin = allowOrigin;
	this.allowMethods = allowMethods;
	this.allowHeaders = allowHeaders;
    }
    
}
