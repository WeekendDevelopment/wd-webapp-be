package com.backend.webapp.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.webapp.mapper.RequestMapper;
import com.backend.webapp.model.RequestStatusEnum;
import com.backend.webapp.model.SignupRequest;
import com.backend.webapp.model.SignupResponse;
import com.backend.webapp.model.Users;
import com.backend.webapp.repository.UsersRepository;
import com.backend.webapp.security.EncryptionUtil;
import com.google.cloud.spring.secretmanager.SecretManagerTemplate;

@RestController
@RequestMapping("/signup")
public class SignupController {

    private static final Logger logger = LogManager.getLogger(LoginController.class);

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private SecretManagerTemplate secretManagerTemplate;

    @SuppressWarnings("rawtypes")
    @PostMapping()
    @CrossOrigin(origins = { "https://wd-webapp-fe.el.r.appspot.com" })
    public ResponseEntity addNewUser(@RequestBody SignupRequest signupRequest) {
        try {
            Users user = usersRepository.findByEmail(signupRequest.getEmail());
            if (user != null) {
                logger.info("User already exists for given email {}", signupRequest.getEmail());
                return ResponseEntity.badRequest()
                        .body(new SignupResponse().status(RequestStatusEnum.FAILED).message("User Already Exists"));
            }
            EncryptionUtil.decryptData(secretManagerTemplate, signupRequest.getPasswordHash());
            usersRepository.save(RequestMapper.mapToUsers(signupRequest));
            return ResponseEntity.ok()
                    .body(new SignupResponse().status(RequestStatusEnum.SUCCESS).message("User Added"));
        } catch (Exception e) {
            logger.error("Failed to add user", e);
            return ResponseEntity.internalServerError()
                    .body(new SignupResponse().status(RequestStatusEnum.FAILED).message("Failed to add user"));
        }
    }

}
