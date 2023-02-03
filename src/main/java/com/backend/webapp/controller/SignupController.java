package com.backend.webapp.controller;

import com.backend.webapp.api.SignupApi;
import com.backend.webapp.document.Users;
import com.backend.webapp.exception.ErrorHandler;
import com.backend.webapp.mapper.RequestMapper;
import com.backend.webapp.model.Error;
import com.backend.webapp.model.SignupRequest;
import com.backend.webapp.model.SignupResponse;
import com.backend.webapp.repository.UsersRepository;
import com.backend.webapp.security.EncryptionUtil;
import com.google.cloud.spring.secretmanager.SecretManagerTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static com.backend.webapp.constant.ErrorConstants.DUPLICATE_EMAIL_ERROR;
import static com.backend.webapp.constant.ErrorConstants.INTERNAL_SERVER_ERROR;

@RestController
public class SignupController extends ErrorHandler implements SignupApi {

    private static final Logger logger = LogManager.getLogger(SignupController.class);

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private SecretManagerTemplate secretManagerTemplate;

    @Override
    public ResponseEntity addNewUser(SignupRequest signupRequest) {
        try {
            Users user = usersRepository.findByEmail(signupRequest.getEmail());
            if (user != null) {
                logger.info("User already exists for given email {}", signupRequest.getEmail());
                return ResponseEntity.badRequest().body(new Error().message(DUPLICATE_EMAIL_ERROR));
            }
            // validating if proper cipher text is received
            EncryptionUtil.decryptData(secretManagerTemplate, signupRequest.getPasswordHash());
            usersRepository.save(RequestMapper.mapToUsers(signupRequest));
            return ResponseEntity.ok().body(new SignupResponse().message("User Added"));
        } catch (Exception e) {
            logger.error("Exception occured while adding user {}", signupRequest.getEmail(), e);
            return ResponseEntity.internalServerError().body(new Error().message(INTERNAL_SERVER_ERROR));
        }
    }

}
