package com.thomasclarisse.client.app.conf;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

import com.thomasclarisse.client.app.json.ObjectMapperProvider;

public class JerseyBootstrap extends ResourceConfig {

    public JerseyBootstrap() {

        // Jackson Mapper
        register(ObjectMapperProvider.class);

        // Features
        register(RequestContextFilter.class);

        // Resources
        packages(true, "com.thomasclarisse.client.app.api");

    }

}