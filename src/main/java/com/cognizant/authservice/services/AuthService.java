package com.cognizant.authservice.services;

import com.cognizant.authservice.dtos.TokenValidationResponse;
import com.cognizant.authservice.dtos.UserCredentialDTO;

public interface AuthService {
    UserCredentialDTO saveUser(UserCredentialDTO userCredentialDTO);
    String generateToken(String userName, String role);
    TokenValidationResponse validateToken(String token);
}
