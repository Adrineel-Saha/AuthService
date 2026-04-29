package com.cognizant.authservice.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.boot.autoconfigure.ssl.SslBundleProperties;

import java.security.Key;
import java.util.Map;

public interface JwtService {
    String createToken(Map<String, Object> claims, String userName);
    Key getSignKey();
    Jws<Claims> validateToken(String token)