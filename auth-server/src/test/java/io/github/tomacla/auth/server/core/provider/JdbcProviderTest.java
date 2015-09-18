package io.github.tomacla.auth.server.core.provider;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import io.github.tomacla.auth.server.core.PasswordEncoding;

public class JdbcProviderTest {

    private static DataSource dataSource;

    private AccountProvider provider;

    @BeforeClass
    public static void before() throws SQLException {
	dataSource = buildDataSource();
	initDatabase();
    }

    @Test
    public void authenticate() {
	provider = new JdbcProvider(dataSource, null, "users", "email", "password");
	Assert.assertTrue(provider.authenticate("john.doe@none.com", "thisisapass"));
    }

    @Test
    public void authenticateWithNonEncoding() {
	provider = new JdbcProvider(dataSource, null, "users", "email", "password", PasswordEncoding.NONE);
	Assert.assertTrue(provider.authenticate("john.doe@none.com", "thisisapass"));
    }

    @Test
    public void authenticateWithMd5Encoding() {
	provider = new JdbcProvider(dataSource, null, "users", "email", "password", PasswordEncoding.MD5);
	Assert.assertTrue(provider.authenticate("john.doe@md5.com", "thisisapass"));
    }

    @Test
    public void authenticateFalse() {
	provider = new JdbcProvider(dataSource, null, "users", "email", "password");
	Assert.assertFalse(provider.authenticate("foo", "bar"));
    }

    private static DataSource buildDataSource() {
	BasicDataSource connectionPool = new BasicDataSource();
	connectionPool.setDriverClassName("org.hsqldb.jdbc.JDBCDriver");
	connectionPool.setUsername("sa");
	connectionPool.setPassword("");
	connectionPool.setUrl("jdbc:hsqldb:mem:testdb");
	connectionPool.setInitialSize(1);
	return connectionPool;
    }

    private static void initDatabase() throws SQLException {
	Connection connection = dataSource.getConnection();
	Statement createStatement = connection.createStatement();
	createStatement.executeQuery("CREATE TABLE users (email varchar(255), password varchar(255));");
	createStatement.executeQuery("INSERT INTO users VALUES('john.doe@none.com', 'thisisapass');");
	createStatement.executeQuery(
		"INSERT INTO users VALUES('john.doe@md5.com', '" + DigestUtils.md5Hex("thisisapass") + "');");
	connection.close();
    }

}