# What is it about

The aim of this project is to build a flexible authentication server with the following features :

* Easy to deploy : a war and a config file in classpath is all you need
* Diversity of authentication providers : in memory, database, ldap and more
* Based on JWT : provide JWT tokens

# Token

This issued token is a JWT token (see http://jwt.io/). It contains a single custom claim field that is named _email_ and contains the account used to generate the token.

# Install

## Application

Get the sources, build the project with maven

	mvn clean install
	
And get the auth-server.war.

## Configuration

The server will search for a auth-server.properties file in the classpath.

This is the sample file with all options included :

	# CSRF CONFIGURATION
	csrf.origin=*
	
	# TOKEN CONFIGURATION
	token.validity=1
	token.issuer=test_auth_server
	token.secret=thisisthedefaultsecretthatmustbeoveriddeninapropertiesfile
	
	# AUTHENTICATION PROVIDERS
	# Accepted values : alwaystrue, inmemory, jdbc, ldap
	# A comma separated list can be provided. The order of authentication will be preserved
	# auth.providers=jdbc,inmemory
	auth.providers=inmemory
	
	# INMEMORY CONFIGURATION
	auth.providers.inmemory.filename=users.json
	
	# JDBC CONFIGURATION
	# Accepted values for password encoding : none, md5
	auth.providers.jdbc.driver=
	auth.providers.jdbc.username=
	auth.providers.jdbc.password=
	auth.providers.jdbc.url=
	auth.providers.jdbc.schema=
	auth.providers.jdbc.table=
	auth.providers.jdbc.column.login=
	auth.providers.jdbc.column.password=
	auth.providers.jdbc.password.encoding=

# Usage

## Use with API

The auth server provider REST API to get and validate tokens.

The API is protected against CSRF by setting the Access-Control-Allow-Headers, Access-Control-Allow-Methods and Access-Control-Allow-Origin headers. The API is able to reply to OPTIONS requests. The _csrf.origin_ parameter in configuration is used to set Access-Control-Allow-Origin (default value is *).

### Create a token

	Post AUTHSERVERPATH/api
	
	REQUEST : 
	Content-Type : application/json
	BODY : {
		"login" : "login",
		"password" : "password"
	}
	
	SUCCESS RESPONSE : 200 (if the provided credentials were valid)
	Content-Type : text/plain
	Body : the JWT Token
	
	ERROR RESPONSE : 400 (if the provided credentials were not valid)
	
### Verify a token

	Get AUTHSERVERPATH/api/{token}
	
	REQUEST :
	Query-Param {token} the token to be checked
	
	SUCCESS RESPONSE : 200 (if the token was issued by this server and is still valid)
	
	ERROR RESPONSE : 400 (if the token was not issued by this server or if the token is not valid anymore)
	
### Get a token from an auth code

	Post AUTHSERVERPATH/api/auth-code
	
	REQUEST : 
	Content-Type : application/json
	BODY : the_auth_code
	
	SUCCESS RESPONSE : 200 (if the auth code was valid)
	Content-Type : text/plain
	Body : the JWT Token
	
	ERROR RESPONSE : 400 (if the auth code was not valid)

## Use with web UI

### Without redirection

The auth server provide an authentication interface that is accessible at _AUTHSERVERPATH/login_.

The user is asked to provide its credentials.

* If the authentication succeeded, the user is redirected to a success page that display the token
* If the authentication failed, the user stays on the same page

### With redirection

A web application can use the auth-server web interface to delegate authentication. The flow to be used is the following :

* Web application redirect the user to AUTHSERVERPATH/login?redirect_to=SOURCEURL
* User provide credentials 
* When the credentials is valid, the user is redirected to SOURCEURL?auth_code=AUTH_CODE
* The web application can exchange the auth code with a token by calling the API
* User is authenticated

## Spring security

The module spring-security-components provide all the needed components to build a spring security configuration using the auth-server. See sample apps for configurations examples.

# Authentication providers

## Always true

**This is only for testing purpose**

This provider does not need configuration and returns always true.

## In memory

This provider is based on a json configuration file to get the list of the credentials.

For example :

	[
		{
			"login" : "tomacla",
			"password" : "tomaclapwd"
		}
	]

## JDBC

This provider rely on a remote database to provide authentication. The configuration must include parameters to connect to the database with JDBC but also the following parameters :

* table : name of the table which contains credentials
* column.login : name of the column of the table containing the login of the user
* column.password : name of the column of the table containing the password of the user
* password.encoding :
  * none : the password is stored in the databse without encoding
  * md5 : the md5 hash of the password is stored in the database

## LDAP

Not yet implemented

# Sample apps

Some sample web applications are provided to illustrate the usage of the auth server.

**The secret is given to these application in order to allow them to directly verify the validity of the token. It is a choice that is not mandatory as the Verify Rest API can be used for that purpose.**

## sample-server-app

This is a web API application that is protected by spring security. A HTTP Parameter X-Token containing a valid token must be provided to request an HTTP endpoint.

See the SecurityConfig class for a spring security sample.

Configuration is set in the sample-server-app.properties file :

	token.secret=thisisthedefaultsecretthatmustbeoveriddeninapropertiesfile
	auth.server.path=http://localhost:8080/auth-server


## sample-client-app-spring

This is a web client application that wants to interact with sample-server-app and is protected by spring security (neither html or javascript pages can be accessed without authentication). A Cookie or HTTP Parameter named X-Token and containing a valid token must be provided to this application. This application is using the auth-server web interface as an authentication form.

See the SecurityConfig class for a spring security sample.

Configuration is set in the sample-client-app.properties file :

	token.secret=thisisthedefaultsecretthatmustbeoveriddeninapropertiesfile
	auth.server.path=http://localhost:8080/auth-server
	client.app.path=http://localhost:8180/sample-server-app

## sample-client-app

This is a web client application that is not protected and wants to interact with the sample-server-app.

Configuration is set in the sample-client-app.properties file :

	token.secret=thisisthedefaultsecretthatmustbeoveriddeninapropertiesfile
	auth.server.path=http://localhost:8080/auth-server
	client.app.path=http://localhost:8180/sample-server-app
