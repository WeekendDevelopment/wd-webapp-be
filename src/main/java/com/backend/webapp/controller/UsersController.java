package com.backend.webapp.controller;

import com.backend.webapp.api.UsersApi;
import com.backend.webapp.document.Users;
import com.backend.webapp.exception.CustomError;
import com.backend.webapp.exception.ErrorHandler;
import com.backend.webapp.model.BaseResponse;
import com.backend.webapp.model.Error;
import com.backend.webapp.model.User;
import com.backend.webapp.service.UserService;
import com.backend.webapp.validator.RequestValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static com.backend.webapp.constant.ApplicationConstants.PASSWORD_MASKED;
import static com.backend.webapp.constant.ErrorConstants.INTERNAL_SERVER_ERROR;

@RestController
public class UsersController extends ErrorHandler implements UsersApi {

    private static final Logger logger = LogManager.getLogger(UsersController.class);

    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity getUserData(String email, String X_REQUEST_USER) {
        try {
            Users user = userService.getUser(email);
            user.setPasswordHash(PASSWORD_MASKED);
            logger.info("User fetch successful for email {}", email);
            return ResponseEntity.ok(user);
        } catch (CustomError e) {
            logger.error("Error occurred on invoking /users with user {}", email, e);
            return ResponseEntity.badRequest().body(new Error().message(e.getErrorMessage()));
        } catch (Exception e) {
            logger.error("Exception occurred while fetching user details for email {}", email, e);
            return ResponseEntity.internalServerError().body(new Error().message(INTERNAL_SERVER_ERROR));
        }
    }

    @Override
    public ResponseEntity updateUserData(String email, String X_REQUEST_USER, User userUpdate) {
        try {
            RequestValidator.validatePatchUserRequest(userUpdate);
            userService.updateUser(email, userUpdate);
            return ResponseEntity.ok(new BaseResponse().message("User Updated"));
        } catch (CustomError e) {
            logger.error("Error occurred on invoking /users with user {}", email, e);
            return ResponseEntity.badRequest().body(new Error().message(e.getErrorMessage()));
        } catch (Exception e) {
            logger.error("Exception occurred while updating user details for email {}", email, e);
            return ResponseEntity.internalServerError().body(new Error().message(INTERNAL_SERVER_ERROR));
        }
    }

}
