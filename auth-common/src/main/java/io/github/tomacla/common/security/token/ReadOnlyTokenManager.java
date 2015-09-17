package io.github.tomacla.common.security.token;

import java.security.Key;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.ReservedClaimNames;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;

public class ReadOnlyTokenManager {

    protected static final String EMAIL_KEY = "email";

    protected String secret;

    public ReadOnlyTokenManager(String secret) {
	this.secret = secret;
    }

    public Optional<TokenDTO> readToken(String token) {

	try {
	    Key key = new HmacKey(secret.getBytes("UTF-8"));

	    JwtConsumer jwtConsumer = new JwtConsumerBuilder().setVerificationKey(key).build();

	    JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
	    Map<String, Object> claimsMap = jwtClaims.getClaimsMap();

	    TokenDTO data = new TokenDTO();
	    data.setEmail((String) claimsMap.get(EMAIL_KEY));
	    data.setIssuer((String) claimsMap.get(ReservedClaimNames.ISSUER));
	    data.setSubject((String) claimsMap.get(ReservedClaimNames.SUBJECT));

	    return Optional.of(data);

	} catch (Exception e) {
	    return Optional.empty();
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

	} catch (Exception e) {
	    return false;
	}
    }

}
