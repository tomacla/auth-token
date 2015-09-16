package io.github.tomacla.auth.server.conf;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

import io.github.tomacla.auth.server.core.provider.AccountProvider;
import io.github.tomacla.auth.server.core.provider.AlwaysTrueProvider;
import io.github.tomacla.auth.server.core.provider.InMemoryProvider;
import io.github.tomacla.auth.server.core.provider.JdbcProvider;
import io.github.tomacla.auth.server.core.provider.LdapProvider;
import io.github.tomacla.auth.server.core.service.AccountService;
import io.github.tomacla.auth.server.core.service.DefaultAccountService;
import io.github.tomacla.common.security.token.TokenManager;

@Configuration
@PropertySource(value = "classpath:auth-server.properties", ignoreResourceNotFound = true)
public class SpringBootstrap {

    protected static final Logger LOGGER = LoggerFactory.getLogger(SpringBootstrap.class);
    
    @Autowired
    private Environment env;
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        return new PropertySourcesPlaceholderConfigurer();
    }
    
    @Bean
    public TokenManager tokenManager() {
	Integer tokenValidityInDays = env.getProperty("token.validity", Integer.class, 15);
	LOGGER.info("Auth server tokens will be valide for {} days", tokenValidityInDays);
	String secret = "6380BA89DA46FB77353BD1DFE0CEB87B43CE978E46B9B9B73AB3881AC954E07A";
	LOGGER.info("A secret has been configure in the code");
	return new TokenManager(secret, tokenValidityInDays);
    }
    
    @Bean
    public List<AccountProvider> accountProviders() {
	String strProvider = env.getProperty("providers", "alwaysTrue");
	if(strProvider == null || strProvider.trim().isEmpty()) {
	    strProvider = "alwaysTrue";
	}
	
	List<AccountProvider> activatedProviders = new ArrayList<>();
	String[] providers = strProvider.split(",");
	for(int i = 0; i < providers.length; i++) {
	    if(providers[i].trim().equals("alwaysTrue")) {
		activatedProviders.add(new AlwaysTrueProvider());
		LOGGER.info("Adding an always true provider (TEST ONLY)");
	    }
	    else if(providers[i].trim().equals("inMemory")) {
		activatedProviders.add(new InMemoryProvider());
		LOGGER.info("Adding a in memory provider");
	    }
	    else if(providers[i].trim().equals("jdbc")) {
		activatedProviders.add(new JdbcProvider());
		LOGGER.info("Adding a jdbc provider");
	    }
	    else if(providers[i].trim().equals("ldap")) {
		activatedProviders.add(new LdapProvider());
		LOGGER.info("Adding a ldap provider");
	    }
	}
	return activatedProviders;
    }
    
    @Bean
    public AccountService accountService() {
	return new DefaultAccountService(tokenManager(), accountProviders());
    }
    
}