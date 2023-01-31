package com.backend.webapp.controller;

import static com.backend.webapp.constant.ErrorConstants.INTERNAL_SERVER_ERROR;
import static com.backend.webapp.model.RequestStatusEnum.FAILED;
import static com.backend.webapp.model.RequestStatusEnum.SUCCESS;
import static com.backend.webapp.constant.ApplicationConstants.PASSWORD_MASKED;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.webapp.exception.CustomError;
import com.backend.webapp.exception.ErrorHandler;
import com.backend.webapp.mapper.RequestMapper;
import com.backend.webapp.model.BaseResponse;
import com.backend.webapp.model.Users;
import com.backend.webapp.repository.UsersRepository;
import com.backend.webapp.util.MongoDocumentFinder;

@RestController
@RequestMapping("/users/{email}")
@Validated
public class UsersController extends ErrorHandler {

    private static final Logger logger = LogManager.getLogger(UsersController.class);

    @Autowired
    private UsersRepository usersRepository;

    @SuppressWarnings("rawtypes")
    @GetMapping()
    public ResponseEntity getUserData(@PathVariable("email") String email) {
        try {
            Users user = MongoDocumentFinder.findDocumentByIdentifier(new Users().email(email), usersRepository);
            user.setPasswordHash(PASSWORD_MASKED);
            return ResponseEntity.ok(user);
        } catch (CustomError e) {
            logger.error("Error occured on invoking /users with user {}", email, e);
            return ResponseEntity.badRequest().body(new BaseResponse().status(FAILED).message(e.getErrorMessage()));
        } catch (Exception e) {
            logger.error("Exception occured while fetching user details for email {}", email, e);
            return ResponseEntity.internalServerError()
                    .body(new BaseResponse().status(FAILED).message(INTERNAL_SERVER_ERROR));
        }
    }

    @SuppressWarnings("rawtypes")
    @PatchMapping()
    public ResponseEntity updateUserData(@PathVariable("email") String email, @RequestBody Users userUpdate) {
        try {
            Users user = MongoDocumentFinder.findDocumentByIdentifier(new Users().email(email), usersRepository);
            // add validations
            usersRepository.save(RequestMapper.mapPatchUserRequest(user, userUpdate));
            return ResponseEntity.ok(new BaseResponse().status(SUCCESS).message("User Updated"));
        } catch (CustomError e) {
            logger.error("Error occured on invoking /users with user {}", email, e);
            return ResponseEntity.badRequest().body(new BaseResponse().status(FAILED).message(e.getErrorMessage()));
        } catch (Exception e) {
            logger.error("Exception occured while updating user details for email {}", email, e);
            return ResponseEntity.internalServerError()
                    .body(new BaseResponse().status(FAILED).message(INTERNAL_SERVER_ERROR));
        }
    }

}
