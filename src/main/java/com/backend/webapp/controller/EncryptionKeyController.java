package com.backend.webapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.webapp.security.EncryptAsymmetric;

@RestController
@RequestMapping("/encryptionKey")
public class EncryptionKeyController {

    @SuppressWarnings("rawtypes")
    @GetMapping()
    @CrossOrigin(origins = { "https://wd-webapp-fe.el.r.appspot.com" })
    public ResponseEntity getPublicKey() {
        try {
            return ResponseEntity.ok(EncryptAsymmetric.getPublicKeyAsString());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to get publicKey from KMS");
        }
    }

}
