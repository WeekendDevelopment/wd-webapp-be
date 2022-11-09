package com.backend.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.webapp.mapper.RequestMapper;
import com.backend.webapp.model.SignupRequest;
import com.backend.webapp.repository.UsersRepository;

@RestController
@RequestMapping("/signup")
public class SignupController {

    @Autowired
    private UsersRepository usersRepository;

    @SuppressWarnings("rawtypes")
    @PostMapping()
    @CrossOrigin(origins = { "https://wd-webapp-fe.el.r.appspot.com" })
    public ResponseEntity addNewUser(@RequestBody SignupRequest signupRequest) {
        try {
            usersRepository.save(RequestMapper.mapToUsers(signupRequest));
            return ResponseEntity.ok().body("User Added");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed");
        }
    }

}
