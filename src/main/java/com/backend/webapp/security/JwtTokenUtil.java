package com.backend.webapp.security;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.backend.webapp.controller.LoginController;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultJwtParser;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import io.micrometer.core.instrument.util.StringUtils;

import static com.backend.webapp.constant.ApplicationConstants.COLON;
import static com.backend.webapp.constant.ApplicationConstants.JWT_EXPIRATION_TIME;
import static com.backend.webapp.constant.ApplicationConstants.SERVICE_NAME;
import static com.backend.webapp.constant.ApplicationConstants.UNPROTECTED_ENDPOINTS;
import static com.backend.webapp.constant.ApplicationConstants.JWT_TOKEN_HEADER;
import static com.backend.webapp.constant.ApplicationConstants.USER_HEADER;

@Component
public class JwtTokenUtil extends OncePerRequestFilter {

    public static final String NAME = "JwtTokenUtil";
    private static final String JWT_SIGNING_SECRET = "REPLACE THIS SECRET WITH A SECURE ONE!!!";
    private static final Logger logger = LogManager.getLogger(LoginController.class);
    private static final JwtParser parser = new DefaultJwtParser();

    private JwtTokenUtil() {
    }

    public static String generateJwtToken(String email) {
        return Jwts.builder().addClaims(null) // add specific claims for specific users later
                .setIssuer(SERVICE_NAME).setSubject(email).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_TIME * 1000))
                .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(getSecret(email).getBytes()))
                .compact();
    }

    private static boolean isValidJwtSignature(String email, String token) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(getSecret(email).getBytes(),
                SignatureAlgorithm.HS512.getJcaName());
        return new DefaultJwtSignatureValidator(SignatureAlgorithm.HS512, secretKeySpec)
                .isValid(splitToken(token, false), splitToken(token, true));
    }

    private static boolean isValidJwt(String email, String token) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        parser.setSigningKey(getSecret(email).getBytes());
        if (parser.parseClaimsJws(token).getBody().getExpiration().before(new Date())) {
            return false;
        }
        return parser.isSigned(token) && isValidJwtSignature(email, token);
    }

    private static String getSecret(String email) {
        return email + COLON + Base64.getEncoder().encodeToString(JWT_SIGNING_SECRET.getBytes());
    }

    private static String splitToken(String token, boolean signature) {
        String[] chunks = token.split("\\.");
        if (signature) {
            return chunks[2];
        }
        return chunks[0] + "." + chunks[1];
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return UNPROTECTED_ENDPOINTS.contains(path);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwtToken = request.getHeader(JWT_TOKEN_HEADER);
        String email = request.getHeader(USER_HEADER);
        try {
            if (isValidJwt(email, jwtToken)) {
                logger.info("JWT is valid... Authorizing User {}", email);
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpStatus.SC_UNAUTHORIZED);
                return;
            }
        } catch (ExpiredJwtException e) {
            logger.error("JWT Token sent by user {} is expired...", email, e);
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            return;
        } catch (Exception e) {
            logger.error("Exception occured while parsing JWT token", e);
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            return;
        }

    }

}
