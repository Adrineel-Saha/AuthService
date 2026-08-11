package com.cognizant.authservice.test.controllers;

import com.cognizant.authservice.controllers.AuthController;
import com.cognizant.authservice.dtos.AuthRequest;
import com.cognizant.authservice.dtos.TokenValidationResponse;
import com.cognizant.authservice.dtos.UserCredentialDTO;
import com.cognizant.authservice.services.AuthService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TestAuthController {
    @Mock
    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthController authController;

    private LocalValidatorFactoryBean validator;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() throws Exception {
        mocks = MockitoAnnotations.openMocks(this);
        validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void testAddNewUserWhenUserIsValid() {
        UserCredentialDTO userCredentialDTO = new UserCredentialDTO();
        userCredentialDTO.setUserName("Suraj");
        userCredentialDTO.setEmail("suraj@example.com");
        userCredentialDTO.setPassword("password123");
        userCredentialDTO.setRole("USER");

        validator.validate(userCredentialDTO).stream().forEach((constraintViolation) -> assertNull(constraintViolation));
    }

    @Test
    void testAddNewUserWhenUserIsNotValid() {
        UserCredentialDTO userCredentialDTO = new UserCredentialDTO();
        userCredentialDTO.setUserName("S");
        userCredentialDTO.setEmail("surajexamplecom");
        userCredentialDTO.setPassword("password123");
        userCredentialDTO.setRole("USER");

        validator.validate(userCredentialDTO).stream().forEach((constraintViolation) -> assertNotNull(constraintViolation));
    }

    @Test
    void testAddNewUserPositiveAssertReturnValue() {
        UserCredentialDTO userCredentialDTO = new UserCredentialDTO();
        userCredentialDTO.setId(1);
        userCredentialDTO.setUserName("Suraj");
        userCredentialDTO.setEmail("suraj@example.com");
        userCredentialDTO.setRole("USER");

        try {
            when(authService.saveUser(any(UserCredentialDTO.class))).thenReturn(userCredentialDTO);
            ResponseEntity<UserCredentialDTO> responseEntity = authController.addNewUser(userCredentialDTO);
            UserCredentialDTO actualUserCredentialDTO = responseEntity.getBody();
            assertNotNull(actualUserCredentialDTO);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void testAddNewUserPositiveAssertStatusCode() {
        UserCredentialDTO userCredentialDTO = new UserCredentialDTO();
        userCredentialDTO.setId(1);
        userCredentialDTO.setUserName("Suraj");
        userCredentialDTO.setEmail("suraj@example.com");
        userCredentialDTO.setRole("USER");

        try {
            when(authService.saveUser(any(UserCredentialDTO.class))).thenReturn(userCredentialDTO);
            ResponseEntity<UserCredentialDTO> responseEntity = authController.addNewUser(userCredentialDTO);
            assertEquals(201, responseEntity.getStatusCode().value());
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void testAddNewUserPositiveAssertUserName() {
        UserCredentialDTO userCredentialDTO = new UserCredentialDTO();
        userCredentialDTO.setId(1);
        userCredentialDTO.setUserName("Suraj");
        userCredentialDTO.setEmail("suraj@example.com");
        userCredentialDTO.setRole("USER");

        try {
            when(authService.saveUser(any(UserCredentialDTO.class))).thenReturn(userCredentialDTO);
            ResponseEntity<UserCredentialDTO> responseEntity = authController.addNewUser(userCredentialDTO);
            assertEquals("Suraj", responseEntity.getBody().getUserName());
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void testAddNewUserNegativeAssertReturnValue() {
        UserCredentialDTO userCredentialDTO = new UserCredentialDTO();

        try {
            when(authService.saveUser(any(UserCredentialDTO.class))).thenReturn(null);
            ResponseEntity<UserCredentialDTO> responseEntity = authController.addNewUser(userCredentialDTO);
            UserCredentialDTO actualUserCredentialDTO = responseEntity.getBody();
            assertNull(actualUserCredentialDTO);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void testAddNewUserNegativeAssertStatusCode() {
        UserCredentialDTO userCredentialDTO = new UserCredentialDTO();

        try {
            when(authService.saveUser(any(UserCredentialDTO.class))).thenReturn(null);
            ResponseEntity<UserCredentialDTO> responseEntity = authController.addNewUser(userCredentialDTO);
            assertEquals(400, responseEntity.getStatusCode().value());
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void testGenerateTokenWhenAuthRequestIsValid() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUserName("Suraj");
        authRequest.setPassword("password123");

        validator.validate(authRequest).stream().forEach((constraintViolation) -> assertNull(constraintViolation));
    }

    @Test
    void testGenerateTokenWhenAuthRequestIsNotValid() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUserName("S");
        authRequest.setPassword("");

        validator.validate(authRequest).stream().forEach((constraintViolation) -> assertNotNull(constraintViolation));
    }

    @Test
    void testGenerateTokenPositiveAssertReturnValue() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUserName("Suraj");
        authRequest.setPassword("password123");

        try {
            Authentication authentication = mock(Authentication.class);
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

            when(authenticationManager.authenticate(any())).thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(true);
            doReturn(authorities).when(authentication).getAuthorities();
            when(authService.generateToken(anyString(), anyString())).thenReturn("dummy-jwt-token");

            ResponseEntity<String> responseEntity = authController.generateToken(authRequest);
            String actualToken = responseEntity.getBody();
            assertNotNull(actualToken);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void testGenerateTokenPositiveAssertStatusCode() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUserName("Suraj");
        authRequest.setPassword("password123");

        try {
            Authentication authentication = mock(Authentication.class);
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

            when(authenticationManager.authenticate(any())).thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(true);
            doReturn(authorities).when(authentication).getAuthorities();
            when(authService.generateToken(anyString(), anyString())).thenReturn("dummy-jwt-token");

            ResponseEntity<String> responseEntity = authController.generateToken(authRequest);
            assertEquals(200, responseEntity.getStatusCode().value());
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void testGenerateTokenPositiveAssertTokenValue() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUserName("Suraj");
        authRequest.setPassword("password123");

        try {
            Authentication authentication = mock(Authentication.class);
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));

            when(authenticationManager.authenticate(any())).thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(true);
            doReturn(authorities).when(authentication).getAuthorities();
            when(authService.generateToken("Suraj", "ADMIN")).thenReturn("dummy-jwt-token");

            ResponseEntity<String> responseEntity = authController.generateToken(authRequest);
            assertEquals("dummy-jwt-token", responseEntity.getBody());
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void testGenerateTokenNegativeAssertReturnValueWhenNotAuthenticated() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUserName("Suraj");
        authRequest.setPassword("wrongPassword");

        try {
            Authentication authentication = mock(Authentication.class);

            when(authenticationManager.authenticate(any())).thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(false);

            ResponseEntity<String> responseEntity = authController.generateToken(authRequest);
            assertNull(responseEntity.getBody());
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void testGenerateTokenNegativeAssertStatusCodeWhenNotAuthenticated() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUserName("Suraj");
        authRequest.setPassword("wrongPassword");

        try {
            Authentication authentication = mock(Authentication.class);

            when(authenticationManager.authenticate(any())).thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(false);

            ResponseEntity<String> responseEntity = authController.generateToken(authRequest);
            assertEquals(401, responseEntity.getStatusCode().value());
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void testGenerateTokenNegativeAssertStatusCodeWhenBadCredentials() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUserName("Suraj");
        authRequest.setPassword("wrongPassword");

        try {
            when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid username or password"));
            authController.generateToken(authRequest);
            assertTrue(false);
        } catch (BadCredentialsException e) {
            assertEquals("Invalid username or password", e.getMessage());
        }
    }

    @Test
    void testValidateTokenPositiveAssertReturnValue() {
        String token = "dummy-jwt-token";
        TokenValidationResponse tokenValidationResponse = new TokenValidationResponse(true, "Suraj", "USER");

        try {
            when(authService.validateToken(token)).thenReturn(tokenValidationResponse);
            ResponseEntity<TokenValidationResponse> responseEntity = authController.validateToken(token);
            TokenValidationResponse actualResponse = responseEntity.getBody();
            assertNotNull(actualResponse);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void testValidateTokenPositiveAssertStatusCode() {
        String token = "dummy-jwt-token";
        TokenValidationResponse tokenValidationResponse = new TokenValidationResponse(true, "Suraj", "USER");

        try {
            when(authService.validateToken(token)).thenReturn(tokenValidationResponse);
            ResponseEntity<TokenValidationResponse> responseEntity = authController.validateToken(token);
            assertEquals(200, responseEntity.getStatusCode().value());
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void testValidateTokenPositiveAssertUserName() {
        String token = "dummy-jwt-token";
        TokenValidationResponse tokenValidationResponse = new TokenValidationResponse(true, "Suraj", "USER");

        try {
            when(authService.validateToken(token)).thenReturn(tokenValidationResponse);
            ResponseEntity<TokenValidationResponse> responseEntity = authController.validateToken(token);
            assertEquals("Suraj", responseEntity.getBody().getUserName());
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void testValidateTokenPositiveAssertRole() {
        String token = "dummy-jwt-token";
        TokenValidationResponse tokenValidationResponse = new TokenValidationResponse(true, "Suraj", "ADMIN");

        try {
            when(authService.validateToken(token)).thenReturn(tokenValidationResponse);
            ResponseEntity<TokenValidationResponse> responseEntity = authController.validateToken(token);
            assertEquals("ADMIN", responseEntity.getBody().getRole());
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void testValidateTokenPositiveAssertValidFlag() {
        String token = "dummy-jwt-token";
        TokenValidationResponse tokenValidationResponse = new TokenValidationResponse(true, "Suraj", "USER");

        try {
            when(authService.validateToken(token)).thenReturn(tokenValidationResponse);
            ResponseEntity<TokenValidationResponse> responseEntity = authController.validateToken(token);
            assertTrue(responseEntity.getBody().isValid());
        } catch (Exception e) {
            assertTrue(false);
        }
    }
}
