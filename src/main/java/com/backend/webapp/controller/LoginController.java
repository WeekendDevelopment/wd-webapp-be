package com.backend.webapp.controller;

import com.backend.webapp.api.LoginApi;
import com.backend.webapp.delegate.impl.UserServiceDelegateImpl;
import com.backend.webapp.exception.CustomError;
import com.backend.webapp.exception.ErrorHandler;
import com.backend.webapp.model.BaseResponse;
import com.backend.webapp.model.Error;
import com.backend.webapp.model.LoginRequest;
import com.backend.webapp.model.LoginResponse;
import com.backend.webapp.security.JwtTokenUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static com.backend.webapp.constant.ErrorConstants.INCORRECT_CREDENTIALS;
import static com.backend.webapp.constant.ErrorConstants.INTERNAL_SERVER_ERROR;

@RestController
public class LoginController extends ErrorHandler implements LoginApi {

    private static final Logger logger = LogManager.getLogger(LoginController.class);

    @Autowired
    private UserServiceDelegateImpl userService;

    @Override
    public ResponseEntity performLogin(LoginRequest loginRequest) {
        try {
            if (userService.verifyUser(loginRequest)) {
                logger.info("Login Success with user {}", loginRequest.getEmail());
                return ResponseEntity
                        .ok(new LoginResponse().signedJwtToken(JwtTokenUtil.generateJwtToken(loginRequest.getEmail()))
                                .message("Login Success"));
            }
        } catch (CustomError e) {
            logger.error("Error occurred on invoking /login with user {}", loginRequest.getEmail(), e);
            return ResponseEntity.badRequest().body(new Error().message(e.getErrorMessage()));
        } catch (Exception e) {
            logger.error("Exception occurred on invoking /login with user {}", loginRequest.getEmail(), e);
            return ResponseEntity.internalServerError().body(new Error().message(INTERNAL_SERVER_ERROR));
        }
        logger.info("Login Failed with user {}", loginRequest.getEmail());
        return ResponseEntity.badRequest().body(new BaseResponse().message(INCORRECT_CREDENTIALS));
    }

}
