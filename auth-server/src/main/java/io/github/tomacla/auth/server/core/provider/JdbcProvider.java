package io.github.tomacla.auth.server.core.provider;

public class JdbcProvider implements AccountProvider {

    private String driver;
    private String url;
    private String username;
    private String password;
    private String database;
    private String schema;
    private String table;
    private String loginColumn;
    private String passwordColumn;
    private String passwordEncoding;

    @Override
    public Boolean authenticate(String login, String password) {
	return false;
    }

    public void setDriver(String driver) {
	this.driver = driver;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public void setDatabase(String database) {
	this.database = database;
    }

    public void setSchema(String schema) {
	this.schema = schema;
    }

    public void setTable(String table) {
	this.table = table;
    }

    public void setLoginColumn(String loginColumn) {
	this.loginColumn = loginColumn;
    }

    public void setPasswordColumn(String passwordColumn) {
	this.passwordColumn = passwordColumn;
    }

    public void setPasswordEncoding(String passwordEncoding) {
	this.passwordEncoding = passwordEncoding;
    }

}