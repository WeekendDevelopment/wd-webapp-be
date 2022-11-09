package com.backend.webapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.webapp.model.HealthStatus;
import com.backend.webapp.model.HealthStatus.Status;
import com.backend.webapp.model.Health;
import com.backend.webapp.repository.HealthRepository;

@RestController
@RequestMapping("/health")
public class HealthController {

    @Autowired
    private HealthRepository healthRepository;

    @SuppressWarnings("rawtypes")
    @GetMapping()
    @CrossOrigin(origins = { "https://wd-webapp-fe.el.r.appspot.com" })
    public ResponseEntity checkHealth() {
        HealthStatus health = new HealthStatus();
        health.setServiceStatus(Status.UP);
        try {
            List<Health> docs = healthRepository.findAll();
            if (docs.isEmpty()) {
                health.setDatabaseStatus(Status.DOWN);
            } else {
                health.setDatabaseStatus(Status.UP);
            }
        } catch (Exception e) {
            health.setDatabaseStatus(Status.DOWN);
        }
        return ResponseEntity.ok(health);
    }

}
