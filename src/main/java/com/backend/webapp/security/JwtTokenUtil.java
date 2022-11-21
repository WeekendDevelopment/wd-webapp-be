package com.backend.webapp.security;

import java.io.Serializable;
import java.util.Base64;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;

import static com.backend.webapp.constant.ApplicationConstants.COLON;
import static com.backend.webapp.constant.ApplicationConstants.JWT_EXPIRATION_TIME;
import static com.backend.webapp.constant.ApplicationConstants.SERVICE_NAME;

public class JwtTokenUtil {
	
	public static final String NAME = "JwtTokenUtil";
	private String secret;
	private String email;
	
	public JwtTokenUtil(String email, String password) {
		this.email = email;
		this.secret = Base64.getEncoder().encodeToString((email + COLON + password).getBytes());
	}
	
	public String generateJwtToken() {
		return Jwts.builder()
				.addClaims(null)
				.setIssuer(SERVICE_NAME)
				.setSubject(this.email)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_TIME * 1000))
				.signWith(SignatureAlgorithm.HS512, this.secret)
				.compact();
	}
	
	public boolean validateJwtToken(String token) {
		String[] chunks = token.split("\\.");
		String signature = chunks[2];
		String tokenWithoutSignature = chunks[0] + "." + chunks[1];
		SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS512.getJcaName());
		return new DefaultJwtSignatureValidator(SignatureAlgorithm.HS512, secretKeySpec).isValid(tokenWithoutSignature, signature);
	}
	
}
