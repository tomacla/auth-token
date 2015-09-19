package io.github.tomacla.common.csrf;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerResponseContext;

public class CsrfUtils {

    private CsrfUtils() {

    }

    public static void writeCsrfHeaders(HttpServletResponse response, String allowOrigin, List<String> allowMethods,
	    List<String> allowHeaders) {
	if (allowOrigin != null && !allowOrigin.isEmpty()) {
	    response.addHeader("Access-Control-Allow-Origin", allowOrigin);
	}
	if (allowMethods != null && allowMethods.size() > 0) {
	    response.addHeader("Access-Control-Allow-Methods", String.join(", ", allowMethods));
	}
	if (allowHeaders != null && allowHeaders.size() > 0) {
	    response.addHeader("Access-Control-Allow-Headers", String.join(", ", allowHeaders));
	}
    }
    
    public static void writeCsrfHeaders(ContainerResponseContext response, String allowOrigin, List<String> allowMethods,
	    List<String> allowHeaders) {
	if (allowOrigin != null && !allowOrigin.isEmpty()) {
	    response.getHeaders().add("Access-Control-Allow-Origin", allowOrigin);
	}
	if (allowMethods != null && allowMethods.size() > 0) {
	    response.getHeaders().add("Access-Control-Allow-Methods", String.join(", ", allowMethods));
	}
	if (allowHeaders != null && allowHeaders.size() > 0) {
	    response.getHeaders().add("Access-Control-Allow-Headers", String.join(", ", allowHeaders));
	}
    }

}
