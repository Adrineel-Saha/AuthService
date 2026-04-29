package com.cognizant.authservice.services;

import com.cognizant.authservice.entities.UserCredential;

public interface AuthService {
    String saveUser(UserCredential credential);
}
