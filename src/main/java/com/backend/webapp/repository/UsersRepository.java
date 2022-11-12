package com.backend.webapp.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.backend.webapp.model.Users;

public interface UsersRepository extends MongoRepository<Users, String> {

    public Users findByUserId(String userId);



    public List<Users> findByEmail(String email);

}
