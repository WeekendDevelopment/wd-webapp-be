package com.backend.webapp.service;

import com.backend.webapp.document.Users;
import com.backend.webapp.exception.CustomError;
import com.backend.webapp.mapper.RequestMapper;
import com.backend.webapp.model.LoginRequest;
import com.backend.webapp.model.SignupRequest;
import com.backend.webapp.model.User;
import com.backend.webapp.repository.UsersRepository;
import com.backend.webapp.security.EncryptionService;
import com.backend.webapp.validator.DocumentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.backend.webapp.constant.ErrorConstants.DUPLICATE_EMAIL_ERROR;

@Service
public class UserService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private EncryptionService encryptionService;

    public Users getUser(String email) throws CustomError {
        Users user = usersRepository.findByEmail(email);
        DocumentValidator.validateUserDocument(user);
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
        encryptionService.decryptData(signupRequest.getPasswordHash());
        usersRepository.save(RequestMapper.mapToUsers(signupRequest));
    }

    public void updateUser(String email, User userUpdate) throws CustomError {
        Users user = this.getUser(email);
        usersRepository.save(RequestMapper.mapPatchUserRequest(user, userUpdate));
    }

    private boolean verifyPassword(String loginRequestPassword, String userDocumentPassword) throws Exception {
        return encryptionService.decryptData(loginRequestPassword)
                .equals(encryptionService.decryptData(userDocumentPassword));
    }

}
