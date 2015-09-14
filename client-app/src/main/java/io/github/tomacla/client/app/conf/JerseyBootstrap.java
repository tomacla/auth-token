package io.github.tomacla.client.app.conf;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

import io.github.tomacla.client.app.json.ObjectMapperProvider;

public class JerseyBootstrap extends ResourceConfig {

    public JerseyBootstrap() {

        // Jackson Mapper
        register(ObjectMapperProvider.class);

        // Features
        register(RequestContextFilter.class);

        // Resources
        packages(true, "io.github.tomacla.client.app.api");

    }

}