package com.backend.webapp.delegate.impl;

import com.backend.webapp.document.Users;
import com.backend.webapp.exception.CustomError;
import com.backend.webapp.mapper.RequestMapper;
import com.backend.webapp.model.LoginRequest;
import com.backend.webapp.model.SignupRequest;
import com.backend.webapp.model.User;
import com.backend.webapp.repository.UsersRepository;
import com.backend.webapp.security.EncryptionUtil;
import com.google.cloud.spring.secretmanager.SecretManagerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.backend.webapp.constant.ApplicationConstants.PASSWORD_MASKED;
import static com.backend.webapp.constant.ErrorConstants.DOCUMENT_NOT_FOUNT_ERROR_DESCRIPTION;
import static com.backend.webapp.constant.ErrorConstants.DUPLICATE_EMAIL_ERROR;

@Component
public class UserServiceDelegateImpl {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private SecretManagerTemplate secretManagerTemplate;

    public Users getUser(String email) throws CustomError {
        Users user = usersRepository.findByEmail(email);
        if (Objects.isNull(user)) {
            throw new CustomError(DOCUMENT_NOT_FOUNT_ERROR_DESCRIPTION);
        }
        return user;
    }

    public boolean verifyUser(LoginRequest loginRequest) throws Exception, CustomError {
        Users user = this.getUser(loginRequest.getEmail());
        return verifyPassword(loginRequest.getPassword(), user.getPasswordHash());
    }

    public void saveUser(SignupRequest signupRequest) throws CustomError, Exception {
        Users user = usersRepository.findByEmail(signupRequest.getEmail());
        if (user != null) {
            throw new CustomError(DUPLICATE_EMAIL_ERROR);
        }
        // validating if proper cipher text is received
        EncryptionUtil.decryptData(secretManagerTemplate, signupRequest.getPasswordHash());
        usersRepository.save(RequestMapper.mapToUsers(signupRequest));
    }

    public void updateUser(String email, User userUpdate) throws CustomError {
        Users user = this.getUser(email);
        usersRepository.save(RequestMapper.mapPatchUserRequest(user, userUpdate));
    }

    private boolean verifyPassword(String loginRequestPassword, String userDocumentPassword) throws Exception {
        return EncryptionUtil.decryptData(secretManagerTemplate, loginRequestPassword)
                .equals(EncryptionUtil.decryptData(secretManagerTemplate, userDocumentPassword));
    }

}
