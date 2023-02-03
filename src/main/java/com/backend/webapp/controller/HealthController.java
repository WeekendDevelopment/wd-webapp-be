package com.backend.webapp.controller;

import com.backend.webapp.api.HealthApi;
import com.backend.webapp.document.Health;
import com.backend.webapp.model.HealthStatus;
import com.backend.webapp.model.Status;
import com.backend.webapp.repository.HealthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HealthController implements HealthApi {

    @Autowired
    private HealthRepository healthRepository;

    @Override
    public ResponseEntity<HealthStatus> getHealth() {
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
