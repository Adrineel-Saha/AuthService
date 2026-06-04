package com.cognizant.authservice.services;

import com.cognizant.authservice.dtos.TokenValidationResponse;
import com.cognizant.authservice.dtos.UserCredentialDTO;
import com.cognizant.authservice.entities.UserCredential;
import com.cognizant.authservice.repositories.UserCredentialRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
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
    public UserCredentialDTO saveUser(UserCredentialDTO userCredentialDTO) {
        UserCredential userCredential=modelMapper.map(userCredentialDTO,UserCredential.class);

        userCredential.setPassword(passwordEncoder.encode(userCredential.getPassword()));
        UserCredential newUserCredential= userCredentialRepository.save(userCredential);

        UserCredentialDTO newUserCredentialDTO=modelMapper.map(newUserCredential, UserCredentialDTO.class);
        return newUserCredentialDTO;
    }

    @Override
    public String generateToken(String userName, String role) {
        return jwtService.generateToken(userName, role);
    }

    @Override
    public TokenValidationResponse validateToken(String token) {
        return jwtService.validateToken(token);
    }
}
