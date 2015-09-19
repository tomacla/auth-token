package io.github.tomacla.server.app.api;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

import io.github.tomacla.server.app.api.json.ObjectMapperProvider;

public class HelloWorldApiTest extends JerseyTest {

    @Configuration
    public static class ApiTestConfiguration {


    }

    @Override
    protected Application configure() {

	AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(ApiTestConfiguration.class);

	ResourceConfig rc = new ResourceConfig();
	rc.property("contextConfig", ctx);
	rc.register(HelloWorldApi.class);
	rc.register(ObjectMapperProvider.class);
	return rc;

    }

    @Test
    public void helloWorld() {
	final Response response = target("/hello").request().get();
	Assert.assertEquals(Status.OK.getStatusCode(), response.getStatusInfo().getStatusCode());
    }

}
