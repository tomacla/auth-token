package com.thomasclarisse.auth.server.conf;

import com.thomasclarisse.auth.server.json.ObjectMapperProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

public class JerseyBootstrap extends ResourceConfig {

    public JerseyBootstrap() {

        // Jackson Mapper
        register(ObjectMapperProvider.class);

        // Features
        register(RequestContextFilter.class);

        // Resources
        packages(true, "com.thomasclarisse.auth.server.api");

    }

}