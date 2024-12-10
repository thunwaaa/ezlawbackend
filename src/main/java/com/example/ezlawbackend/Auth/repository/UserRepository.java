package com.example.ezlawbackend.Auth.repository;

import com.example.ezlawbackend.Auth.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
}