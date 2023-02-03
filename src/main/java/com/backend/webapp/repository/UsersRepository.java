package com.backend.webapp.repository;

import com.backend.webapp.document.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsersRepository extends MongoRepository<Users, String> {

    public Users findByEmail(String email);

}
