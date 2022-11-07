package com.backend.webapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.webapp.model.Health;
import com.backend.webapp.model.Health.Status;

@RestController
@RequestMapping("/health")
public class HealthController {

	@SuppressWarnings("rawtypes")
	@GetMapping()
	@CrossOrigin(origins = { "https://wd-webapp-fe.el.r.appspot.com" })
	public ResponseEntity checkHealth() {
		Health health = new Health();
		health.setStatus(Status.UP);
		return ResponseEntity.ok(health);
	}
	
}
