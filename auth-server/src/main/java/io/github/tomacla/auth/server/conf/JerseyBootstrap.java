package io.github.tomacla.auth.server.conf;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

public class JerseyBootstrap extends ResourceConfig {

    public JerseyBootstrap() {
	register(RequestContextFilter.class);
	packages(true, "io.github.tomacla.auth.server.api");
    }

}