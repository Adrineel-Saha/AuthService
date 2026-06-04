package com.cognizant.authservice.config;

import com.cognizant.authservice.entities.UserCredential;
import com.cognizant.authservice.repositories.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserCredentialRepository userCredentialRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserCredential> userCredential= userCredentialRepository.findByUserName(username);
        return userCredential.map(CustomUserDetails::new).orElseThrow(()->new RuntimeException("User not found with name: " + username));
    }
}
