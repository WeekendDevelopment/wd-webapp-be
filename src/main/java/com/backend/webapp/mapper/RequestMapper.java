package com.backend.webapp.mapper;

import org.apache.commons.lang3.StringUtils;

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

    public static Users mapPatchUserRequest(Users user, Users userUpdate) {
        if (StringUtils.isNotBlank(userUpdate.getFullName())) {
            user.setFullName(userUpdate.getFullName());
        }
        if (StringUtils.isNotBlank(userUpdate.getAbout())) {
            user.setAbout(userUpdate.getAbout());
        }
        if (StringUtils.isNotBlank(userUpdate.getCountry())) {
            user.setCountry(userUpdate.getCountry());
        }
        if (StringUtils.isNotBlank(userUpdate.getPhoneNumber())) {
            user.setPhoneNumber(userUpdate.getPhoneNumber());
        }
        if (StringUtils.isNotBlank(userUpdate.getStatus())) {
            user.setStatus(userUpdate.getStatus());
        }
        return user;
    }

}
