package io.github.tomacla.auth.server.conf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

import io.github.tomacla.auth.server.api.csrf.CsrfConfig;
import io.github.tomacla.auth.server.core.PasswordEncoding;
import io.github.tomacla.auth.server.core.Providers;
import io.github.tomacla.auth.server.core.provider.AccountProvider;
import io.github.tomacla.auth.server.core.provider.AlwaysTrueProvider;
import io.github.tomacla.auth.server.core.provider.InMemoryProvider;
import io.github.tomacla.auth.server.core.provider.JdbcProvider;
import io.github.tomacla.auth.server.core.provider.LdapProvider;
import io.github.tomacla.auth.server.core.service.AccountService;
import io.github.tomacla.auth.server.core.service.DefaultAccountService;
import io.github.tomacla.common.token.TokenManager;

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
    public CsrfConfig csrfConfig() {
	return new CsrfConfig(getAllowOrigin(), getAllowMethods(), getAllowHeaders());
    }

    @Bean
    public TokenManager tokenManager() {
	Integer tokenValidityInDays = env.getProperty("token.validity", Integer.class, 15);
	LOGGER.info("Auth server tokens will be valide for {} days", tokenValidityInDays);
	String secret = env.getProperty("token.secret",
		"thisisthedefaultsecretthatmustbeoveriddeninapropertiesfile");
	LOGGER.info("A secret has been configure in the code");
	return new TokenManager(secret, tokenValidityInDays);
    }

    @Bean
    public List<AccountProvider> accountProviders() {
	String strProvider = env.getProperty("auth.providers", Providers.ALWAYS_TRUE.toString());
	if (strProvider == null || strProvider.trim().isEmpty()) {
	    strProvider = Providers.ALWAYS_TRUE.toString();
	}

	List<AccountProvider> activatedProviders = new ArrayList<>();
	String[] providers = strProvider.split(",");
	for (int i = 0; i < providers.length; i++) {
	    Providers provider = Providers.fromString(providers[i].trim());
	    if (provider != null) {
		if (provider.equals(Providers.ALWAYS_TRUE)) {
		    activatedProviders.add(new AlwaysTrueProvider());
		    LOGGER.info("Adding an always true provider (TEST ONLY)");
		} else if (provider.equals(Providers.IN_MEMORY)) {
		    activatedProviders.add(new InMemoryProvider(env.getProperty("auth.providers.inmemory.filename", "users.json")));
		    LOGGER.info("Adding a in memory provider");
		} else if (provider.equals(Providers.JDBC)) {
		    activatedProviders.add(new JdbcProvider(buildDataSource(),
			    env.getProperty("auth.providers.jdbc.schema"),
			    env.getProperty("auth.providers.jdbc.table"),
			    env.getProperty("auth.providers.jdbc.column.login"),
			    env.getProperty("auth.providers.jdbc.column.password"),
			    PasswordEncoding.fromString(env.getProperty("auth.providers.jdbc.password.encoding", "none"))));
		    LOGGER.info("Adding a jdbc provider");
		} else if (provider.equals(Providers.LDAP)) {
		    activatedProviders.add(new LdapProvider());
		    LOGGER.info("Adding a ldap provider");
		}
	    } else {
		throw new RuntimeException("Unable to load a " + providers[i].trim() + " authentication provider");
	    }
	}
	return activatedProviders;
    }

    @Bean
    public AccountService accountService() {
	String issuerName = env.getProperty("token.issuer", "AUTH_SERVER");
	 LOGGER.info("Auth server issuer name is {}", issuerName);
	return new DefaultAccountService(issuerName, tokenManager(), accountProviders());
    }

    private DataSource buildDataSource() {
	BasicDataSource connectionPool = new BasicDataSource();
	connectionPool.setDriverClassName(env.getProperty("auth.providers.jdbc.driver"));
	connectionPool.setUsername(env.getProperty("auth.providers.jdbc.username"));
	connectionPool.setPassword(env.getProperty("auth.providers.jdbc.password"));
	connectionPool.setUrl(env.getProperty("auth.providers.jdbc.url"));
	connectionPool.setInitialSize(1);
	return connectionPool;
    }
    
    private String getAllowOrigin() {
	String allowOrigin = env.getProperty("csrf.origin", "*");
	LOGGER.info("CSRF Allowed Origin are {}", allowOrigin);
	return allowOrigin;
    }
    
    private List<String> getAllowHeaders() {
	return Arrays.asList("Content-Type", "Accept");
    }
    
    private List<String> getAllowMethods() {
	return Arrays.asList("GET", "POST", "OPTIONS");
    }

}