package io.github.tomacla.auth.server.api;

import java.util.Optional;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.tomacla.auth.server.api.dto.AuthRequestDTO;
import io.github.tomacla.auth.server.api.json.ObjectMapperProvider;
import io.github.tomacla.auth.server.core.service.DefaultAccountService;

public class AuthenticationApiTest extends JerseyTest {

    @Configuration
    public static class ApiTestConfiguration {

	@Bean
	public DefaultAccountService authenticationService() {
	    return Mockito.mock(DefaultAccountService.class);
	}

    }

    @Override
    protected Application configure() {

	AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(ApiTestConfiguration.class);
	authServiceMock = ctx.getBean(DefaultAccountService.class);

	ResourceConfig rc = new ResourceConfig();
	rc.property("contextConfig", ctx);
	rc.register(AuthenticationApi.class);
	rc.register(ObjectMapperProvider.class);
	return rc;

    }

    private DefaultAccountService authServiceMock;

    @Test
    public void authenticateByCredentials() {

	Mockito.when(authServiceMock.authenticate("foo", "bar")).thenReturn(Optional.of("fake_code"));

	final Response response = target("/").request()
		.post(Entity.json(AuthRequestDTO.get("foo", "bar")));

	Assert.assertEquals(Status.OK.getStatusCode(), response.getStatusInfo().getStatusCode());
	Assert.assertEquals("fake_code", response.readEntity(String.class));

	response.close();
    }

    @Test
    public void authenticateByCredentials_fail() {

	Mockito.when(authServiceMock.authenticate("foo", "bar")).thenReturn(Optional.empty());

	final Response response = target("/").request()
		.post(Entity.json(AuthRequestDTO.get("foo", "bar")));

	Assert.assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatusInfo().getStatusCode());

	response.close();
    }

    @Test
    public void authenticateByToken() {

	Mockito.when(authServiceMock.verifyToken("fake_token")).thenReturn(Optional.of("foo"));

	final Response response = target("/fake_token").request().get();

	Assert.assertEquals(Status.OK.getStatusCode(), response.getStatusInfo().getStatusCode());

	response.close();
    }

    @Test
    public void authenticateByToken_fail() {

	Mockito.when(authServiceMock.verifyToken("fake_token")).thenReturn(Optional.empty());

	final Response response = target("/fake_token").request().get();

	Assert.assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatusInfo().getStatusCode());

	response.close();
    }

}
