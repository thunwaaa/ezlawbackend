package com.example.ezlawbackend.Auth.repository;

import com.example.ezlawbackend.Auth.model.UserMySQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserMySQLRepository extends JpaRepository<UserMySQL, Long> {
    Optional<UserMySQL> findByEmail(String email);
}
