package com.backend.webapp.controller;

import com.backend.webapp.api.UsersApi;
import com.backend.webapp.document.Users;
import com.backend.webapp.exception.CustomError;
import com.backend.webapp.exception.ErrorHandler;
import com.backend.webapp.mapper.RequestMapper;
import com.backend.webapp.model.BaseResponse;
import com.backend.webapp.model.Error;
import com.backend.webapp.model.User;
import com.backend.webapp.repository.UsersRepository;
import com.backend.webapp.util.MongoDocumentFinder;
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
    private UsersRepository usersRepository;

    @Override
    public ResponseEntity getUserData(String email) {
        try {
            Users user = MongoDocumentFinder.findDocumentByIdentifier(new Users().email(email), usersRepository);
            user.setPasswordHash(PASSWORD_MASKED);
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
    public ResponseEntity updateUserData(String email, User userUpdate) {
        try {
            Users user = MongoDocumentFinder.findDocumentByIdentifier(new Users().email(email), usersRepository);
            // add validations
            usersRepository.save(RequestMapper.mapPatchUserRequest(user, userUpdate));
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
