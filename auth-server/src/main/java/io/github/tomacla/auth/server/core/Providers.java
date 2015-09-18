package io.github.tomacla.auth.server.core;

public enum Providers {

    ALWAYS_TRUE("alwaystrue"), IN_MEMORY("inmemory"), JDBC("jdbc"), LDAP("ldap");

    private String text;

    private Providers(String text) {
	this.text = text;
    }

    public static Providers fromString(String text) {
	if (text != null) {
	    for (Providers b : Providers.values()) {
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