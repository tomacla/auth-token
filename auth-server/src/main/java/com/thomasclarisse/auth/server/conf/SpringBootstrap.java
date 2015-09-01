package com.thomasclarisse.auth.server.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.thomasclarisse.auth.server")
public class SpringBootstrap {

    private Logger logger = LoggerFactory.getLogger(SpringBootstrap.class);

}