package com.backend.webapp.constant;

import java.util.List;

public final class ApplicationConstants {

    private ApplicationConstants() {
    }

    public static final String COLON = ":";
    public static final String SERVICE_NAME = "wd-webapp-be";

    public static final String PASSWORD_MASKED = "**********";

    // 2 hours validity time
    public static final long JWT_EXPIRATION_TIME = 2 * 60 * 60;
    public static final String JWT_TOKEN_HEADER = "Authorization"; // for sending jwt token
    public static final String USER_HEADER = "X-REQUEST-USER"; // for sending current user email who logged in
    public static final List<String> UNPROTECTED_ENDPOINTS = List.of("/login", "/signup", "/encryptionKey", "/health");

}
