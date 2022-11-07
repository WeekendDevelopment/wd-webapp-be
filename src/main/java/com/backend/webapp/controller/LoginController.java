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

@RestController
@RequestMapping("/login")
public class LoginController {
	
	@Autowired
	private UsersRepository usersRepository;

	@SuppressWarnings("rawtypes")
	@PostMapping()
	@CrossOrigin(origins = { "https://wd-webapp-fe.el.r.appspot.com" })
	public ResponseEntity performLogin(@RequestBody LoginRequest loginRequest) {
		Users user = usersRepository.findByUserId(loginRequest.getUsername());
		if(null != user && 
				null != user.getUserId() && 
				user.getUserId().equalsIgnoreCase(loginRequest.getUsername()) && 
				null != user.getPasswordHash() && 
				user.getPasswordHash().equalsIgnoreCase(loginRequest.getPassword())) {
			return ResponseEntity.ok("Success");
		}
		return ResponseEntity.badRequest().body("Failed");
	}
	
}
