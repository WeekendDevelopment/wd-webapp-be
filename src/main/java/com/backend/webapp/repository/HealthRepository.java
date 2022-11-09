package com.backend.webapp.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.backend.webapp.model.Health;

public interface HealthRepository extends MongoRepository<Health, String> {

    public List<Health> findByHealth(String health);

}
