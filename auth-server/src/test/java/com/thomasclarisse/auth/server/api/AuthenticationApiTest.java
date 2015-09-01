package com.thomasclarisse.auth.server.api;

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

import com.thomasclarisse.auth.server.api.dto.AuthRequestDTO;
import com.thomasclarisse.auth.server.json.ObjectMapperProvider;
import com.thomasclarisse.auth.server.service.AuthenticationService;



public class AuthenticationApiTest extends JerseyTest {

	@Configuration
	public static class ApiTestConfiguration {
		
		@Bean
		public AuthenticationService authenticationService() {
			return Mockito.mock(AuthenticationService.class);
		}
		
	}
	
	@Override
	protected Application configure() {
		
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(ApiTestConfiguration.class);
		authServiceMock = ctx.getBean(AuthenticationService.class);
		
		ResourceConfig rc = new ResourceConfig();
		rc.property("contextConfig", ctx);
		rc.register(AuthenticationApi.class);
		rc.register(ObjectMapperProvider.class);
		return rc;
		
	}

	private AuthenticationService authServiceMock;
	
	@Test
	public void authenticate() {
		Mockito.when(authServiceMock.authenticate("foo", "bar")).thenReturn(Optional.of("fake_code"));
		
		final Response response = target("/auth")
				.queryParam("resultType", "code")
				.request()
				.post(Entity.json(AuthRequestDTO.get("foo", "bar")));
		
		Assert.assertEquals(Status.OK.getStatusCode(), response.getStatusInfo().getStatusCode());
		Assert.assertEquals("fake_code", response.readEntity(String.class));
	}
	
}
