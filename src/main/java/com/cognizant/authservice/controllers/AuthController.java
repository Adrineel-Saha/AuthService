package com.cognizant.authservice.controllers;

import com.cognizant.authservice.dtos.AuthRequest;
import com.cognizant.authservice.dtos.TokenValidationResponse;
import com.cognizant.authservice.dtos.UserCredentialDTO;
import com.cognizant.authservice.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@Tag(
        name="CRUD REST APIs for Auth Service",
        description="CRUD REST APIs - Register User, Get JWT Token, Validate JWT Token"
)
@CrossOrigin(originPatterns = { "http://*:9191", "https://*:9191", "http://*:4200", "https://*:4200" })
@Slf4j
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("register")
    @Operation(
            summary="Register User REST API",
            description="Used to register user to the database"
    )
    public ResponseEntity<UserCredentialDTO> addNewUser(@Valid @RequestBody UserCredentialDTO userCredentialDTO){
        log.info("Inside Register");

        UserCredentialDTO createdUserCredentialDTO =authService.saveUser(userCredentialDTO);

        if(createdUserCredentialDTO !=null){
            return new ResponseEntity<>(createdUserCredentialDTO, HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("token")
    @Operation(
            summary="Get JWT Token REST API",
            description="Used to get JWT token from the database"
    )
    public ResponseEntity<String> generateToken(@Valid @RequestBody AuthRequest authRequest){
        log.info("Inside Generate Token");

        Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));

        if(authentication.isAuthenticated()) {
            log.info("Inside Authenticated");
            String role = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse("ROLE_USER");
            String token = authService.generateToken(authRequest.getUserName(), role);
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("validate/{token}")
    @Operation(
            summary="Validate JWT Token REST API",
            description="Used to validate JWT token and return username and role"
    )
    public ResponseEntity<TokenValidationResponse> validateToken(@PathVariable("token") String token){
        log.info("Inside Validate Token");

        TokenValidationResponse response = authService.validateToken(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
