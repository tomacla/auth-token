package io.github.tomacla.auth.server.core.provider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.tomacla.auth.server.core.PasswordEncoding;

public class JdbcProvider implements AccountProvider {

    protected static final Logger LOGGER = LoggerFactory.getLogger(JdbcProvider.class);

    private DataSource dataSource;
    private String schema;
    private String table;
    private String loginColumn;
    private String passwordColumn;
    private PasswordEncoding passwordEncoding;

    public JdbcProvider(DataSource dataSource, String schema, String table, String loginColumn, String passwordColumn) {
	this(dataSource, schema, table, loginColumn, passwordColumn, PasswordEncoding.NONE);
    }

    public JdbcProvider(DataSource dataSource, String schema, String table, String loginColumn, String passwordColumn,
	    PasswordEncoding passwordEncoding) {
	this.dataSource = dataSource;
	this.schema = schema;
	this.table = table;
	this.loginColumn = loginColumn;
	this.passwordColumn = passwordColumn;
	this.passwordEncoding = passwordEncoding;
    }

    @Override
    public Boolean authenticate(String login, String password) {

	try {
	    Connection conn = getConnection();
	    PreparedStatement prepareStatement = conn.prepareStatement(this.getSelectRequest());
	    prepareStatement.setString(1, login);

	    ResultSet rs = prepareStatement.executeQuery();
	    if (rs.next()) {
		String inDatabasePassword = rs.getString(passwordColumn);
		if (passwordEncoding.equals(PasswordEncoding.NONE)) {
		    return inDatabasePassword.equals(password);
		}
		if (passwordEncoding.equals(PasswordEncoding.MD5)) {
		    return inDatabasePassword.equals(DigestUtils.md5Hex(password));
		}
	    }
	    return false;

	} catch (SQLException e) {
	    LOGGER.error("An error occured while authentication against database", e);
	    return false;
	}
    }

    private String getSelectRequest() {
	return "SELECT " + loginColumn + "," + passwordColumn + " FROM " + table + " WHERE " + loginColumn + "=?";
    }

    private Connection getConnection() throws SQLException {
	Connection conn = dataSource.getConnection();
	if (this.schema != null) {
	    conn.setSchema(this.schema);
	}
	return conn;
    }

}