package com.cognizant.authservice.repositories;

import com.cognizant.authservice.entities.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCredentialRepository extends JpaRepository<UserCredential, Integer> {
    Optional<UserCredential> findByUserName(String userName);
}
