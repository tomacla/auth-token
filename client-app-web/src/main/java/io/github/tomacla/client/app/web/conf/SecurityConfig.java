package io.github.tomacla.client.app.web.conf;

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

import io.github.tomacla.common.security.entrypoint.RedirectEntryPoint;
import io.github.tomacla.common.security.filter.AuthCodeProcessingFilter;
import io.github.tomacla.common.security.filter.FilterPosition;
import io.github.tomacla.common.security.filter.TokenCookieWriterFilter;
import io.github.tomacla.common.security.filter.TokenProcessing;
import io.github.tomacla.common.security.filter.TokenProcessingFilter;
import io.github.tomacla.common.security.provider.TokenAuthenticationProvider;
import io.github.tomacla.common.security.token.ReadOnlyTokenManager;
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
		.addFilterAfter(authCodeProcessingFilter(), FilterPosition.PRE_AUTH)
		.addFilterAfter(tokenProcessingFilter(), FilterPosition.PRE_AUTH)
		.addFilterAfter(tokenCookieWriterFilter(), FilterPosition.LAST) //
		.exceptionHandling().authenticationEntryPoint(redirectEntryPoint()).and().authorizeRequests()
		.antMatchers("/**").authenticated();
    }

    @Bean
    public TokenAuthenticationProvider authenticationProvider() {
	return new TokenAuthenticationProvider(tokenService());
    }

    @Bean
    public TokenCookieWriterFilter tokenCookieWriterFilter() {
	return new TokenCookieWriterFilter();
    }

    @Bean
    public ReadOnlyTokenManager readOnlyTokenManager() {
	// TODO put this somewhere else
	String secret = "6380BA89DA46FB77353BD1DFE0CEB87B43CE978E46B9B9B73AB3881AC954E07A";
	LOGGER.info("A secret has been configure in the code");
	return new ReadOnlyTokenManager(secret);
    }

    @Bean
    public TokenService tokenService() {
	String authServerRootUrl = env.getProperty("auth.server.path", "http://localhost:8080/auth-server");
	LOGGER.info("Auth server is located at {}", authServerRootUrl);
	return new DefaultTokenService(readOnlyTokenManager(), authServerRootUrl);
    }

    @Bean
    public TokenProcessingFilter tokenProcessingFilter() {
	return new TokenProcessingFilter(TokenProcessing.BOTH);
    }

    @Bean
    public AuthCodeProcessingFilter authCodeProcessingFilter() {
	return new AuthCodeProcessingFilter(tokenService());
    }

    @Bean
    public RedirectEntryPoint redirectEntryPoint() {
	String authServerRedirectUrl = env.getProperty("auth.server.path", "http://localhost:8080/auth-server");
	authServerRedirectUrl += "/login?redirect_to=%s";
	LOGGER.info("Redirect URL for login is located at {}", authServerRedirectUrl);
	return new RedirectEntryPoint(authServerRedirectUrl);
    }

}