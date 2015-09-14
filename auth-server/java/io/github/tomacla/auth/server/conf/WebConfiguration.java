package io.github.tomacla.auth.server.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

@EnableWebMvc
@Configuration
@ComponentScan("io.github.tomacla.auth.server.web")
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	registry.addResourceHandler("/static/**").addResourceLocations("/static/");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
	configurer.enable();
    }

    @Bean
    public ViewResolver viewResolver() {

	ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
	resolver.setPrefix("/WEB-INF/pages/");
	resolver.setSuffix(".html");
	resolver.setTemplateMode("HTML5");
	resolver.setOrder(1);

	SpringTemplateEngine engine = new SpringTemplateEngine();
	engine.setTemplateResolver(resolver);

	ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
	viewResolver.setTemplateEngine(engine);
	return viewResolver;
    }

}
