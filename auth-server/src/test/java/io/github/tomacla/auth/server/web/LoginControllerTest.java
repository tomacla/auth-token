package io.github.tomacla.auth.server.web;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import io.github.tomacla.auth.server.conf.WebConfiguration;
import io.github.tomacla.auth.server.core.service.AccountService;
import io.github.tomacla.auth.server.web.LoginControllerTest.LoginControllerTestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { WebConfiguration.class, LoginControllerTestConfig.class })
@WebAppConfiguration
public class LoginControllerTest {

    @Configuration
    public static class LoginControllerTestConfig {
	
	@Bean
	public AccountService accountService() {
	    return Mockito.mock(AccountService.class);
	}
	
    }
    
    @Autowired
    private WebApplicationContext wac;

    protected MockMvc mockMvc;

    @Before
    public void setup() {
	this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void getAccount() throws Exception {
	this.mockMvc.perform(get("/login")
		.accept(MediaType.TEXT_HTML))
		.andExpect(view().name("login"))
		.andExpect(model().attribute("credentials", notNullValue()));
    }
    
    // TODO finish this test

}
