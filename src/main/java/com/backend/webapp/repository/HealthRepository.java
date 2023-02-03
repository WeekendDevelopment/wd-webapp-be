package com.backend.webapp.repository;

import com.backend.webapp.document.Health;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HealthRepository extends MongoRepository<Health, String> {

    public List<Health> findByHealth(String health);

}
