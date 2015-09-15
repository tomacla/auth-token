package io.github.tomacla.client.app.conf;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

public class JerseyBootstrap extends ResourceConfig {

    public JerseyBootstrap() {

	register(RequestContextFilter.class);

	packages(true, "io.github.tomacla.client.app.api");

    }

}