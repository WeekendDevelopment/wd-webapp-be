package com.backend.webapp.mapper;

import com.backend.webapp.model.SignupRequest;
import com.backend.webapp.model.Users;

public final class RequestMapper {

    private RequestMapper() {
        // private constructor to avoid instantiation
    }

    public static Users mapToUsers(SignupRequest signupRequest) {
        Users user = new Users();
        user.setEmail(signupRequest.getEmail());
        user.setPasswordHash(signupRequest.getPasswordHash());
        user.setRole(signupRequest.getRole());
        user.setFullName(signupRequest.getFullName());
        return user;
    }

}
