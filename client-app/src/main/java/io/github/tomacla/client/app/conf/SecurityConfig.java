package io.github.tomacla.client.app.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import io.github.tomacla.common.security.entrypoint.UnauthorizedEntryPoint;
import io.github.tomacla.common.security.filter.FilterPosition;
import io.github.tomacla.common.security.filter.TokenProcessingFilter;
import io.github.tomacla.common.security.provider.TokenAuthenticationProvider;
import io.github.tomacla.common.service.DefaultTokenService;
import io.github.tomacla.common.service.TokenService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

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
	return new DefaultTokenService();
    }

    @Bean
    public TokenProcessingFilter tokenProcessingFilter() {
	return new TokenProcessingFilter(true, false);
    }
    
    @Bean
    public UnauthorizedEntryPoint unauthorizedEntryPoint() {
	return new UnauthorizedEntryPoint();
    }

}