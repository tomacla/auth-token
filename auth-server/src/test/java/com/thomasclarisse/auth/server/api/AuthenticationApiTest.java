package com.thomasclarisse.auth.server.api;

import java.util.Optional;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Form;
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
	public void displayLoginForm() {
		
		final Response response = target("/auth/login")
				.request()
				.header("Referer", "http://www.test.com")
				.get();
		
		// TODO test if referer not here
		
		Assert.assertEquals(Status.OK.getStatusCode(), response.getStatusInfo().getStatusCode());
		
		response.close();
		
	}
	
	@Test
	public void receiveLoginForm() {
		
		Mockito.when(authServiceMock.authenticate("foo", "bar")).thenReturn(Optional.of("fake_code"));
		
		Form form = new Form().param("login", "foo").param("password", "bar").param("redirectTo", "http://www.test.com");
		
		final Response response = target("/auth/login")
				.request()
				.header("Referer", "http://www.test.com")
				.post(Entity.form(form));
		
		Assert.assertEquals(Status.OK.getStatusCode(), response.getStatusInfo().getStatusCode());
		Assert.assertEquals("http://www.test.com?code=fake_code",  response.readEntity(String.class));
		
		response.close();
				
	}
	
	@Test
	public void receiveLoginForm_fail() {
		
		Mockito.when(authServiceMock.authenticate("foo", "bar")).thenReturn(Optional.empty());
		
		Form form = new Form().param("login", "foo").param("password", "bar").param("redirectTo", "http://www.test.com");
		
		final Response response = target("/auth/login")
				.request()
				.header("Referer", "http://www.test.com")
				.post(Entity.form(form));
		
		Assert.assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatusInfo().getStatusCode());
		
		response.close();
				
	}
	
	@Test
	public void authenticateByCredentials() {
		
		Mockito.when(authServiceMock.authenticate("foo", "bar")).thenReturn(Optional.of("fake_code"));
		
		final Response response = target("/auth/credentials")
				.queryParam("resultType", "code")
				.request()
				.post(Entity.json(AuthRequestDTO.get("foo", "bar")));
		
		Assert.assertEquals(Status.OK.getStatusCode(), response.getStatusInfo().getStatusCode());
		Assert.assertEquals("fake_code", response.readEntity(String.class));
		
		response.close();
	}
	
	@Test
	public void authenticateByCredentials_fail() {
		
		Mockito.when(authServiceMock.authenticate("foo", "bar")).thenReturn(Optional.empty());
		
		final Response response = target("/auth/credentials")
				.queryParam("resultType", "code")
				.request()
				.post(Entity.json(AuthRequestDTO.get("foo", "bar")));
		
		Assert.assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatusInfo().getStatusCode());
		
		response.close();
		
	}
	
	@Test
	public void authenticateByCode() {
		
		Mockito.when(authServiceMock.getTokenFromAuthCode("fake_code")).thenReturn(Optional.of("fake_token"));
		
		final Response response = target("/auth/code")
				.queryParam("resultType", "token")
				.request()
				.post(Entity.json("fake_code"));
		
		Assert.assertEquals(Status.OK.getStatusCode(), response.getStatusInfo().getStatusCode());
		Assert.assertEquals("fake_token", response.readEntity(String.class));
		
		response.close();
	}
	
	@Test
	public void authenticateByCode_fail() {
		
		Mockito.when(authServiceMock.getTokenFromAuthCode("fake_code")).thenReturn(Optional.empty());
		
		final Response response = target("/auth/code")
				.queryParam("resultType", "token")
				.request()
				.post(Entity.json("fake_code"));
		
		Assert.assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatusInfo().getStatusCode());
		
		response.close();
	}
	
}
