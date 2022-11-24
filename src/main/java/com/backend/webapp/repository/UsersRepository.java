package com.backend.webapp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.backend.webapp.model.Users;

public interface UsersRepository extends MongoRepository<Users, String> {

    public Users findByEmail(String email);

}
