package io.github.tomacla.client.app.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import io.github.tomacla.client.app.resource.RemoteResourceUrlTransformer;

@EnableWebMvc
@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private Environment env;
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	registry.addResourceHandler("/static/**").addResourceLocations("/static/");
	registry.addResourceHandler("/*.html").addResourceLocations("/static/").resourceChain(false)
	.addTransformer(remoteResourceUrlTransformer());
    }

    @Bean
    public RemoteResourceUrlTransformer remoteResourceUrlTransformer() {
	String remoteApiUrl = env.getProperty("client.app.path", "http://localhost:8080/sample-server-app");
	String authServerUrl = env.getProperty("auth.server.path", "http://localhost:8080/auth-server");
	return new RemoteResourceUrlTransformer(remoteApiUrl, authServerUrl);
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
	configurer.enable();
    }

}