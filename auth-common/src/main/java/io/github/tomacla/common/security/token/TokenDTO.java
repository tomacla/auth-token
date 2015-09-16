package io.github.tomacla.common.security.token;

public class TokenDTO {

    private String subject;
    private String issuer;
    private String email;

    public String getSubject() {
	return subject;
    }

    public void setSubject(String subject) {
	this.subject = subject;
    }

    public String getIssuer() {
	return issuer;
    }

    public void setIssuer(String issuer) {
	this.issuer = issuer;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

}
