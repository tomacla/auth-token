package io.github.tomacla.client.app.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import io.github.tomacla.common.security.entrypoint.UnauthorizedEntryPoint;
import io.github.tomacla.common.security.filter.FilterPosition;
import io.github.tomacla.common.security.filter.TokenProcessing;
import io.github.tomacla.common.security.filter.TokenProcessingFilter;
import io.github.tomacla.common.security.provider.TokenAuthenticationProvider;
import io.github.tomacla.common.service.DefaultTokenService;
import io.github.tomacla.common.service.TokenService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    protected static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);
    
    @Autowired
    private Environment env;
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
	http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		.addFilterAfter(tokenProcessingFilter(), FilterPosition.PRE_AUTH)
		.exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint()).and()
		.authorizeRequests().antMatchers("/**").authenticated();
    }

    @Bean
    public TokenAuthenticationProvider authenticationProvider() {
	return new TokenAuthenticationProvider(tokenService());
    }
    
    @Bean
    public TokenService tokenService() {
	String authServerRootUrl = env.getProperty("auth.server.path", "http://localhost:8080/auth-server");
	LOGGER.info("Auth server is located at {}", authServerRootUrl);
	return new DefaultTokenService(authServerRootUrl);
    }

    @Bean
    public TokenProcessingFilter tokenProcessingFilter() {
	return new TokenProcessingFilter(TokenProcessing.HTTP_PARAM);
    }
    
    @Bean
    public UnauthorizedEntryPoint unauthorizedEntryPoint() {
	return new UnauthorizedEntryPoint();
    }

}