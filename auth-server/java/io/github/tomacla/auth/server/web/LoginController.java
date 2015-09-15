package io.github.tomacla.auth.server.web;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.tomacla.auth.server.core.service.DefaultAccountService;

@Controller
public class LoginController {

    protected static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
    
    @Autowired
    private DefaultAccountService authService;

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String showLoginForm(@RequestParam(name = "redirect_to", required = false) String redirectTo, Model model) {
	LOGGER.debug("Display login form");
	if (redirectTo == null || redirectTo.trim().isEmpty()) {
	    redirectTo = "nowhere";
	}

	CredentialsWrapper credentials = new CredentialsWrapper();
	credentials.redirectTo = redirectTo;

	model.addAttribute("message", "Provide your credentials :");
	model.addAttribute("credentials", credentials);
	return "login";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String handleLoginForm(CredentialsWrapper credentials, Model model) {
	LOGGER.debug("Handle post request from login form");
	Optional<String> token = authService.authenticate(credentials.login, credentials.password);
	if (token.isPresent()) {
	    if (credentials.redirectTo.equals("nowhere")) {
		model.addAttribute("token", token.get());
		return "success";
	    }
	    String authCode = authService.getAuthCodeForToken(token.get());
	    // TODO redirection is wrong here, should check if url already have
	    // query params or not
	    return "redirect:" + credentials.redirectTo + "?auth_code=" + authCode;
	}
	model.addAttribute("message", "Login failed : check your credentials");
	model.addAttribute("credentials", credentials);
	return "login";
    }

    @RequestMapping(path = "/success", method = RequestMethod.GET)
    public String showSuccessPage(Model model) {
	LOGGER.debug("Show success page");
	return "success";
    }

    public static class CredentialsWrapper {

	private String login;
	private String password;
	private String redirectTo;

	public String getLogin() {
	    return login;
	}

	public void setLogin(String login) {
	    this.login = login;
	}

	public String getPassword() {
	    return password;
	}

	public void setPassword(String password) {
	    this.password = password;
	}

	public String getRedirectTo() {
	    return redirectTo;
	}

	public void setRedirectTo(String redirectTo) {
	    this.redirectTo = redirectTo;
	}

    }

}
