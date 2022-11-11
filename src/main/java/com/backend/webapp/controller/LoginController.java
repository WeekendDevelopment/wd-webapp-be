package com.backend.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.webapp.model.LoginRequest;
import com.backend.webapp.model.Users;
import com.backend.webapp.repository.UsersRepository;
import com.backend.webapp.security.EncryptionUtil;
import com.google.cloud.spring.secretmanager.SecretManagerTemplate;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private SecretManagerTemplate secretManagerTemplate;

    @SuppressWarnings("rawtypes")
    @PostMapping()
    @CrossOrigin(origins = { "https://wd-webapp-fe.el.r.appspot.com" })
    public ResponseEntity performLogin(@RequestBody LoginRequest loginRequest) {
        if (null == loginRequest.getUsername() || null == loginRequest.getPassword()) {
            return ResponseEntity.badRequest().body("Improper Request");
        }
        try {
            String password = EncryptionUtil.decryptData(secretManagerTemplate, loginRequest.getPassword());
            Users user = usersRepository.findByUserId(loginRequest.getUsername());
            if (null != user && null != user.getUserId()
                    && user.getUserId().equalsIgnoreCase(loginRequest.getUsername())
                    && null != user.getPasswordHash()) {
                String decryptedPassword = EncryptionUtil.decryptData(secretManagerTemplate, user.getPasswordHash());
                if (password.equalsIgnoreCase(decryptedPassword)) {
                    return ResponseEntity.ok("Success");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Exception occured");
        }
        return ResponseEntity.badRequest().body("Login Failed");
    }

}
