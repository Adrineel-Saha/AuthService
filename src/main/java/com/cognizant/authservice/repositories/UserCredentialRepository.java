package com.cognizant.authservice.repositories;

import com.cognizant.authservice.entities.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCredentialRepository extends JpaRepository<UserCredential, Integer> {
}
