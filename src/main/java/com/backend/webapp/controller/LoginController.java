package com.backend.webapp.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.webapp.exception.ErrorHandler;
import com.backend.webapp.model.LoginRequest;
import com.backend.webapp.model.LoginResponse;
import com.backend.webapp.model.Users;
import com.backend.webapp.repository.UsersRepository;
import com.backend.webapp.security.EncryptionUtil;
import com.backend.webapp.security.JwtTokenUtil;
import com.google.cloud.spring.secretmanager.SecretManagerTemplate;

import static com.backend.webapp.model.RequestStatusEnum.FAILED;
import static com.backend.webapp.model.RequestStatusEnum.SUCCESS;

import javax.validation.Valid;

import static com.backend.webapp.constant.ErrorConstants.INTERNAL_SERVER_ERROR;
import static com.backend.webapp.constant.ErrorConstants.INCORRECT_CREDENTIALS;

@RestController
@RequestMapping("/login")
@Validated
public class LoginController extends ErrorHandler {

    private static final Logger logger = LogManager.getLogger(LoginController.class);

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private SecretManagerTemplate secretManagerTemplate;

    @SuppressWarnings("rawtypes")
    @PostMapping()
    @CrossOrigin(origins = { "https://wd-webapp-fe.el.r.appspot.com" })
    public ResponseEntity performLogin(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            String password = EncryptionUtil.decryptData(secretManagerTemplate, loginRequest.getPassword());
            Users user = usersRepository.findByEmail(loginRequest.getEmail());
            if (null != user && null != user.getEmail() && user.getEmail().equalsIgnoreCase(loginRequest.getEmail())
                    && null != user.getPasswordHash()) {
                if (password.equals(EncryptionUtil.decryptData(secretManagerTemplate, user.getPasswordHash()))) {
                    logger.info("Login Success with user {}", loginRequest.getEmail());
                    return ResponseEntity
                            .ok(new LoginResponse().signedJwtToken(JwtTokenUtil.generateJwtToken(user.getEmail()))
                                    .status(SUCCESS).message("Login Success"));
                }
            }
        } catch (Exception e) {
            logger.error("Exception occured on invoking /login with user {}", loginRequest.getEmail(), e);
            return ResponseEntity.internalServerError()
                    .body(new LoginResponse().status(FAILED).message(INTERNAL_SERVER_ERROR));
        }
        logger.info("Login Failed with user {}", loginRequest.getEmail());
        return ResponseEntity.badRequest().body(new LoginResponse().status(FAILED).message(INCORRECT_CREDENTIALS));
    }

}
