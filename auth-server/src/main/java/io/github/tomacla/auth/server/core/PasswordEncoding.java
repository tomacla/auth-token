package io.github.tomacla.auth.server.core;

public enum PasswordEncoding {

    NONE("none"), MD5("md5");

    private String text;

    private PasswordEncoding(String text) {
	this.text = text;
    }

    public static PasswordEncoding fromString(String text) {
	if (text != null) {
	    for (PasswordEncoding b : PasswordEncoding.values()) {
		if (text.equalsIgnoreCase(b.text)) {
		    return b;
		}
	    }
	}
	return null;
    }

    @Override
    public String toString() {
	return this.text;
    }

}