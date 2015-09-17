package io.github.tomacla.auth.server.api.dto;

public class AuthRequestDTO {

    public String login;
    public String password;

    public static AuthRequestDTO get(String login, String password) {
	AuthRequestDTO dto = new AuthRequestDTO();
	dto.login = login;
	dto.password = password;
	return dto;
    }

}
