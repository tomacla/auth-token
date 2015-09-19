package io.github.tomacla.spring.security.filter;

public enum TokenProcessing {

    HTTP_PARAM(false, true), COOKIE(true, false), BOTH(true, true);
    
    private Boolean supportCookie;
    private Boolean supportHttpParam;
    
    private TokenProcessing(Boolean supportCookie, Boolean supportHttpParam) {
	this.supportCookie = supportCookie;
	this.supportHttpParam = supportHttpParam;
    }

    public Boolean supportCookie() {
        return supportCookie;
    }

    public Boolean supportHttpParam() {
        return supportHttpParam;
    }
    
}
