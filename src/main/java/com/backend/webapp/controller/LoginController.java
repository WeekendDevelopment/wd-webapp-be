package com.backend.webapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.webapp.model.LoginRequest;

@RestController
@RequestMapping("/login")
public class LoginController {

	@SuppressWarnings("rawtypes")
	@PostMapping()
	@CrossOrigin(origins = { "https://wd-webapp-fe.el.r.appspot.com" })
	public ResponseEntity performLogin(@RequestBody LoginRequest loginRequest) {
		if("admin".equalsIgnoreCase(loginRequest.getUsername()) 
				&& "12345".equalsIgnoreCase(loginRequest.getPassword())) {
			return ResponseEntity.ok("Success");
		}
		return ResponseEntity.badRequest().body("Failed");
	}
	
}
