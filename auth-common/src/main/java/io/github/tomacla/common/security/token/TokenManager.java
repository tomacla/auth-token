package io.github.tomacla.common.security.token;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.ReservedClaimNames;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;

public class TokenManager {

    protected static final String EMAIL_KEY = "email";

    private Integer daysOfValidity;
    private String secret;
    
    public TokenManager(String secret, Integer daysOfValidity) {
	this.daysOfValidity = daysOfValidity;
	this.secret = secret;
    }

    public String getToken(TokenDTO data) {

	try {

	    ZonedDateTime expiration = ZonedDateTime.now(ZoneOffset.UTC).plus(daysOfValidity, ChronoUnit.DAYS);

	    Key key = new HmacKey(secret.getBytes("UTF-8"));

	    JwtClaims claims = new JwtClaims();
	    claims.setExpirationTime(NumericDate.fromMilliseconds(expiration.toInstant().toEpochMilli()));
	    claims.setGeneratedJwtId();
	    claims.setIssuedAtToNow();
	    claims.setNotBeforeMinutesInThePast(2);
	    claims.setSubject(data.getSubject());
	    claims.setIssuer(data.getIssuer());
	    claims.setClaim(EMAIL_KEY, data.getEmail());

	    JsonWebSignature jws = new JsonWebSignature();
	    jws.setPayload(claims.toJson());
	    jws.setKey(key);
	    jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);

	    return jws.getCompactSerialization();

	} catch (JoseException | UnsupportedEncodingException e) {
	    throw new RuntimeException("An error occured while building a token", e);
	}

    }

    public TokenDTO readToken(String token) {

	try {
	    Key key = new HmacKey(secret.getBytes("UTF-8"));

	    JwtConsumer jwtConsumer = new JwtConsumerBuilder().setVerificationKey(key).build();

	    JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
	    Map<String, Object> claimsMap = jwtClaims.getClaimsMap();

	    TokenDTO data = new TokenDTO();
	    data.setEmail((String) claimsMap.get(EMAIL_KEY));
	    data.setIssuer((String) claimsMap.get(ReservedClaimNames.ISSUER));
	    data.setSubject((String) claimsMap.get(ReservedClaimNames.SUBJECT));

	    return data;

	} catch (UnsupportedEncodingException | InvalidJwtException e) {
	    throw new RuntimeException("An error occured while reading token", e);
	}
    }

    public Boolean isValid(String token) {

	try {
	    Key key = new HmacKey(secret.getBytes("UTF-8"));

	    ZonedDateTime expiration = ZonedDateTime.now(ZoneOffset.UTC);

	    JwtConsumer jwtConsumer = new JwtConsumerBuilder().setVerificationKey(key).setRequireExpirationTime()
		    .setEvaluationTime(NumericDate.fromMilliseconds(expiration.toInstant().toEpochMilli())).build();

	    jwtConsumer.process(token);

	    return true;

	} catch (InvalidJwtException e) {
	    return false;
	} catch (UnsupportedEncodingException e) {
	    throw new RuntimeException("An error occured while reading token", e);
	}
    }

}
