package com.cognizant.authservice.services;

import com.cognizant.authservice.dtos.TokenValidationResponse;

import java.security.Key;

public interface JwtService {
    String generateToken(String userName, String role);
    TokenValidationResponse validateToken(String token);
    Key getSignKey();
}