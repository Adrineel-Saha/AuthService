package com.cognizant.authservice.services;

import com.cognizant.authservice.dtos.TokenValidationResponse;
import com.cognizant.authservice.dtos.UserCredentialDTO;
import com.cognizant.authservice.entities.UserCredential;
import com.cognizant.authservice.exceptions.RateLimitExceededException;
import com.cognizant.authservice.repositories.UserCredentialRepository;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService{
    @Autowired
    private UserCredentialRepository userCredentialRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    @RateLimiter(name = "AuthServiceRateLimiter", fallbackMethod = "saveUserFallback")
    public UserCredentialDTO saveUser(UserCredentialDTO userCredentialDTO) {
        UserCredential userCredential=modelMapper.map(userCredentialDTO,UserCredential.class);

        userCredential.setPassword(passwordEncoder.encode(userCredential.getPassword()));
        UserCredential newUserCredential= userCredentialRepository.save(userCredential);

        UserCredentialDTO newUserCredentialDTO=modelMapper.map(newUserCredential, UserCredentialDTO.class);
        return newUserCredentialDTO;
    }

    @Override
    @RateLimiter(name = "AuthServiceRateLimiter", fallbackMethod = "generateTokenFallback")
    public String generateToken(String userName, String role) {
        return jwtService.generateToken(userName, role);
    }

    @Override
    @RateLimiter(name = "AuthServiceRateLimiter", fallbackMethod = "validateTokenFallback")
    public TokenValidationResponse validateToken(String token) {
        return jwtService.validateToken(token);
    }

    // ---- Rate limiter fallbacks ----
    // Signature rule: same params as the original method + RequestNotPermitted as the last argument.

    public UserCredentialDTO saveUserFallback(UserCredentialDTO userCredentialDTO, RequestNotPermitted ex) {
        throw rateLimitExceeded("saveUser", ex);
    }

    public String generateTokenFallback(String userName, String role, RequestNotPermitted ex) {
        throw rateLimitExceeded("generateToken", ex);
    }

    public TokenValidationResponse validateTokenFallback(String token, RequestNotPermitted ex) {
        throw rateLimitExceeded("validateToken", ex);
    }

    private RateLimitExceededException rateLimitExceeded(String operation, RequestNotPermitted ex) {
        log.warn("Rate limit exceeded for {}: {}", operation, ex.getMessage());
        return new RateLimitExceededException("Too many requests. Please try again later.");
    }
}
