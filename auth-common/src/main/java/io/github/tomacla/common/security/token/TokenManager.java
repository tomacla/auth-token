package io.github.tomacla.common.security.token;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;

public class TokenManager extends ReadOnlyTokenManager {

    private Integer daysOfValidity;
    
    public TokenManager(String secret, Integer daysOfValidity) {
	super(secret);
	this.daysOfValidity = daysOfValidity;
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

}
