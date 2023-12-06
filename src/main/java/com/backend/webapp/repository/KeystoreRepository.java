package com.backend.webapp.repository;

import com.backend.webapp.document.Keystore;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface KeystoreRepository extends MongoRepository<Keystore, String> {

    List<Keystore> findByName(String name);

}
