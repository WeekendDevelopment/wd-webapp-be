package com.backend.webapp.controller;

import com.backend.webapp.api.SignupApi;
import com.backend.webapp.exception.CustomError;
import com.backend.webapp.exception.ErrorHandler;
import com.backend.webapp.model.Error;
import com.backend.webapp.model.SignupRequest;
import com.backend.webapp.model.SignupResponse;
import com.backend.webapp.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static com.backend.webapp.constant.ErrorConstants.INTERNAL_SERVER_ERROR;

@RestController
public class SignupController extends ErrorHandler implements SignupApi {

    private static final Logger logger = LogManager.getLogger(SignupController.class);

    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity addNewUser(SignupRequest signupRequest) {
        try {
            userService.saveUser(signupRequest);
            logger.info("User {} added successfully", signupRequest.getEmail());
            return ResponseEntity.ok().body(new SignupResponse().message("User Added"));
        } catch (CustomError e) {
            logger.error("Exception occured while adding user {}", signupRequest.getEmail(), e);
            return ResponseEntity.badRequest().body(new Error().message(e.getErrorMessage()));
        } catch (Exception e) {
            logger.error("Exception occured while adding user {}", signupRequest.getEmail(), e);
            return ResponseEntity.internalServerError().body(new Error().message(INTERNAL_SERVER_ERROR));
        }
    }

}
