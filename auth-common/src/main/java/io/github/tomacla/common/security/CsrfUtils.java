package io.github.tomacla.common.security;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

public class CsrfUtils {

    private CsrfUtils() {

    }

    public static void writeCsrfHeaders(HttpServletResponse response, String allowOrigin, List<String> allowMethods,
	    List<String> allowHeaders) {
	response.addHeader("Access-Control-Allow-Origin", allowOrigin);
	response.addHeader("Access-Control-Allow-Methods", String.join(", ", allowMethods));
	response.addHeader("Access-Control-Allow-Headers", String.join(", ", allowHeaders));
    }

}
