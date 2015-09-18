package io.github.tomacla.auth.server.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.Assert;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import io.github.tomacla.auth.server.conf.WebConfiguration;
import io.github.tomacla.auth.server.core.service.AccountService;
import io.github.tomacla.auth.server.web.LoginController.CredentialsWrapper;
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
    
    @Autowired
    private AccountService accountService;
    
    protected MockMvc mockMvc;

    @Before
    public void setup() {
	this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void showLoginForm() throws Exception {
	MvcResult result = this.mockMvc.perform(get("/login")
		.accept(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		.andReturn();
	
	ModelAndView mv = result.getModelAndView();
	Assert.assertEquals("login", mv.getViewName());
	Assert.assertNull(((CredentialsWrapper)mv.getModel().get("credentials")).getLogin());
	Assert.assertNull(((CredentialsWrapper)mv.getModel().get("credentials")).getPassword());
	Assert.assertNull(((CredentialsWrapper)mv.getModel().get("credentials")).getRedirectTo());
    }
    
    @Test
    public void showLoginFormWithRedirect() throws Exception {
	MvcResult result = this.mockMvc.perform(get("/login?redirect_to=/path")
		.accept(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		.andReturn();
	
	ModelAndView mv = result.getModelAndView();
	Assert.assertEquals("login", mv.getViewName());
	Assert.assertNull(((CredentialsWrapper)mv.getModel().get("credentials")).getLogin());
	Assert.assertNull(((CredentialsWrapper)mv.getModel().get("credentials")).getPassword());
	Assert.assertEquals("/path", ((CredentialsWrapper)mv.getModel().get("credentials")).getRedirectTo());
    }
    
    @Test
    public void handleLoginForm() throws Exception {
	
	Mockito.when(accountService.authenticate("john.doe", "foo")).thenReturn(Optional.of("token"));
	
	MvcResult result = this.mockMvc.perform(post("/login")
		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("login", "john.doe")
                .param("password", "foo")
		.accept(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		.andReturn();
	
	ModelAndView mv = result.getModelAndView();
	Assert.assertEquals("success", mv.getViewName());
	Assert.assertEquals("token", ((String)mv.getModel().get("token")));
    }
    
    @Test
    public void handleLoginFormBullshitUrl() throws Exception {
	
	Mockito.when(accountService.authenticate("john.doe", "foo")).thenReturn(Optional.of("token"));
	
	MvcResult result = this.mockMvc.perform(post("/login")
		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("login", "john.doe")
                .param("password", "foo")
                .param("redirectTo", "bullshit")
		.accept(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		.andReturn();
	
	ModelAndView mv = result.getModelAndView();
	Assert.assertEquals("login", mv.getViewName());
	Assert.assertEquals("john.doe", ((CredentialsWrapper)mv.getModel().get("credentials")).getLogin());
	Assert.assertEquals("foo", ((CredentialsWrapper)mv.getModel().get("credentials")).getPassword());
	Assert.assertNull(((CredentialsWrapper)mv.getModel().get("credentials")).getRedirectTo());
    }
    
    @Test
    public void handleLoginFormWithRedirectNoParams() throws Exception {
	
	Mockito.when(accountService.authenticate("john.doe", "foo")).thenReturn(Optional.of("token"));
	Mockito.when(accountService.getAuthCodeForToken("token")).thenReturn("code");
	
	MvcResult result = this.mockMvc.perform(post("/login")
		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("login", "john.doe")
                .param("password", "foo")
                .param("redirectTo", "http://www.valid.org/do")
		.accept(MediaType.TEXT_HTML))
		.andExpect(status().is3xxRedirection())
		.andReturn();
	
	Assert.assertEquals(302, result.getResponse().getStatus());
	Assert.assertEquals("http://www.valid.org/do?auth_code=code", result.getResponse().getRedirectedUrl());
	
    }
    
    @Test
    public void handleLoginFormWithRedirectWithParams() throws Exception {
	
	Mockito.when(accountService.authenticate("john.doe", "foo")).thenReturn(Optional.of("token"));
	Mockito.when(accountService.getAuthCodeForToken("token")).thenReturn("code");
	
	MvcResult result = this.mockMvc.perform(post("/login")
		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("login", "john.doe")
                .param("password", "foo")
                .param("redirectTo", "http://www.valid.org/do?foo=bar")
		.accept(MediaType.TEXT_HTML))
		.andExpect(status().is3xxRedirection())
		.andReturn();
	
	Assert.assertEquals(302, result.getResponse().getStatus());
	Assert.assertEquals("http://www.valid.org/do?foo=bar&auth_code=code", result.getResponse().getRedirectedUrl());
	
    }
    
    @Test
    public void handleLoginFormError() throws Exception {
	
	Mockito.when(accountService.authenticate("john.doe", "foo")).thenReturn(Optional.empty());
	
	MvcResult result = this.mockMvc.perform(post("/login")
		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("login", "john.doe")
                .param("password", "foo")
		.accept(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		.andReturn();
	
	ModelAndView mv = result.getModelAndView();
	Assert.assertEquals("login", mv.getViewName());
	Assert.assertEquals("john.doe", ((CredentialsWrapper)mv.getModel().get("credentials")).getLogin());
	Assert.assertEquals("foo", ((CredentialsWrapper)mv.getModel().get("credentials")).getPassword());
	Assert.assertNull(((CredentialsWrapper)mv.getModel().get("credentials")).getRedirectTo());
    }
    
    @Test
    public void handleLoginFormErrorWithRedirect() throws Exception {
	
	Mockito.when(accountService.authenticate("john.doe", "foo")).thenReturn(Optional.empty());
	
	MvcResult result = this.mockMvc.perform(post("/login")
		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("login", "john.doe")
                .param("password", "foo")
                .param("redirectTo", "/path")
		.accept(MediaType.TEXT_HTML))
		.andExpect(status().isOk())
		.andReturn();
	
	ModelAndView mv = result.getModelAndView();
	Assert.assertEquals("login", mv.getViewName());
	Assert.assertEquals("john.doe", ((CredentialsWrapper)mv.getModel().get("credentials")).getLogin());
	Assert.assertEquals("foo", ((CredentialsWrapper)mv.getModel().get("credentials")).getPassword());
	Assert.assertEquals("/path", ((CredentialsWrapper)mv.getModel().get("credentials")).getRedirectTo());
    }
    

}
