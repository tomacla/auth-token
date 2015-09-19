package io.github.tomacla.server.app.conf;

import java.util.Arrays;
import java.util.List;

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

import io.github.tomacla.common.header.Headers;
import io.github.tomacla.common.token.ReadOnlyTokenManager;
import io.github.tomacla.spring.security.entrypoint.CsrfOrUnauthorizedEntryPoint;
import io.github.tomacla.spring.security.filter.CsrfHeadersWriterFilter;
import io.github.tomacla.spring.security.filter.FilterPosition;
import io.github.tomacla.spring.security.filter.TokenProcessing;
import io.github.tomacla.spring.security.filter.TokenProcessingFilter;
import io.github.tomacla.spring.security.provider.TokenAuthenticationProvider;
import io.github.tomacla.spring.security.service.DefaultTokenService;
import io.github.tomacla.spring.security.service.TokenService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    protected static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);
    
    @Autowired
    private Environment env;
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	auth.authenticationProvider(authenticationProvider()).userDetailsService(tokenService());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
	http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		.addFilterAfter(tokenProcessingFilter(), FilterPosition.PRE_AUTH)
		.addFilterAfter(csrfHeadersWriterFilter(), FilterPosition.LAST)
		.exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint()).and()
		.authorizeRequests().antMatchers("/**").authenticated();
    }
    
    @Bean
    public ReadOnlyTokenManager readOnlyTokenManager() {
	String secret = env.getProperty("token.secret", "thisisthedefaultsecretthatmustbeoveriddeninapropertiesfile");
	LOGGER.info("A secret has been configure in the code");
	return new ReadOnlyTokenManager(secret);
    }
    
    @Bean
    public TokenAuthenticationProvider authenticationProvider() {
	return new TokenAuthenticationProvider(tokenService());
    }
    
    @Bean
    public CsrfHeadersWriterFilter csrfHeadersWriterFilter() {
	return new CsrfHeadersWriterFilter(getAllowOrigin(), getAllowMethods(), getAllowHeaders());
    }
    
    @Bean
    public TokenService tokenService() {
	String authServerRootUrl = env.getProperty("auth.server.path", "http://localhost:8080/auth-server");
	LOGGER.info("Auth server is located at {}", authServerRootUrl);
	return new DefaultTokenService(readOnlyTokenManager(), authServerRootUrl);
    }

    @Bean
    public TokenProcessingFilter tokenProcessingFilter() {
	return new TokenProcessingFilter(TokenProcessing.HTTP_PARAM);
    }
    
    @Bean
    public CsrfOrUnauthorizedEntryPoint unauthorizedEntryPoint() {
	return new CsrfOrUnauthorizedEntryPoint(getAllowOrigin(), getAllowMethods(), getAllowHeaders());
    }
    
    private String getAllowOrigin() {
	return "*";
    }
    
    private List<String> getAllowHeaders() {
	return Arrays.asList(Headers.X_TOKEN, "Content-Type", "Accept");
    }
    
    private List<String> getAllowMethods() {
	return Arrays.asList("GET", "OPTIONS");
    }
    

}