package com.cognizant.authservice.test.services;

import com.cognizant.authservice.dtos.TokenValidationResponse;
import com.cognizant.authservice.dtos.UserCredentialDTO;
import com.cognizant.authservice.entities.UserCredential;
import com.cognizant.authservice.exceptions.RateLimitExceededException;
import com.cognizant.authservice.repositories.UserCredentialRepository;
import com.cognizant.authservice.services.AuthServiceImpl;
import com.cognizant.authservice.services.JwtService;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TestAuthServiceImpl {
    @Mock
    private UserCredentialRepository userCredentialRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    private Validator validator;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Test
    void testSaveUserPositive() {
        try {
            UserCredentialDTO userCredentialDTO = new UserCredentialDTO();
            userCredentialDTO.setUserName("Akash");
            userCredentialDTO.setEmail("akash@example.com");
            userCredentialDTO.setPassword("password123");
            userCredentialDTO.setRole("USER");

            UserCredential userCredential = new UserCredential();
            userCredential.setUserName("Akash");
            userCredential.setEmail("akash@example.com");
            userCredential.setPassword("password123");
            userCredential.setRole("USER");

            UserCredential savedUserCredential = new UserCredential();
            savedUserCredential.setId(1);
            savedUserCredential.setUserName("Akash");
            savedUserCredential.setEmail("akash@example.com");
            savedUserCredential.setPassword("encodedPassword");
            savedUserCredential.setRole("USER");

            UserCredentialDTO savedUserCredentialDTO = new UserCredentialDTO();
            savedUserCredentialDTO.setId(1);
            savedUserCredentialDTO.setUserName("Akash");
            savedUserCredentialDTO.setEmail("akash@example.com");
            savedUserCredentialDTO.setRole("USER");

            when(modelMapper.map(any(UserCredentialDTO.class), eq(UserCredential.class))).thenReturn(userCredential);
            when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
            when(userCredentialRepository.save(any(UserCredential.class))).thenReturn(savedUserCredential);
            when(modelMapper.map(any(UserCredential.class), eq(UserCredentialDTO.class))).thenReturn(savedUserCredentialDTO);

            UserCredentialDTO actualUserCredentialDTO = authServiceImpl.saveUser(userCredentialDTO);
            assertNotNull(actualUserCredentialDTO);
        } catch (Exception ex) {
            assertTrue(false);
        }
    }

    @Test
    void testSaveUserPositiveAssertId() {
        try {
            UserCredentialDTO userCredentialDTO = new UserCredentialDTO();
            userCredentialDTO.setUserName("Akash");
            userCredentialDTO.setEmail("akash@example.com");
            userCredentialDTO.setPassword("password123");
            userCredentialDTO.setRole("USER");

            UserCredential userCredential = new UserCredential();

            UserCredential savedUserCredential = new UserCredential();
            savedUserCredential.setId(1);

            UserCredentialDTO savedUserCredentialDTO = new UserCredentialDTO();
            savedUserCredentialDTO.setId(1);
            savedUserCredentialDTO.setUserName("Akash");
            savedUserCredentialDTO.setEmail("akash@example.com");

            when(modelMapper.map(any(UserCredentialDTO.class), eq(UserCredential.class))).thenReturn(userCredential);
            when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
            when(userCredentialRepository.save(any(UserCredential.class))).thenReturn(savedUserCredential);
            when(modelMapper.map(any(UserCredential.class), eq(UserCredentialDTO.class))).thenReturn(savedUserCredentialDTO);

            UserCredentialDTO actualUserCredentialDTO = authServiceImpl.saveUser(userCredentialDTO);
            assertEquals(1, actualUserCredentialDTO.getId());
        } catch (Exception ex) {
            assertTrue(false);
        }
    }

    @Test
    void testSaveUserPositiveAssertPasswordIsEncoded() {
        try {
            UserCredentialDTO userCredentialDTO = new UserCredentialDTO();
            userCredentialDTO.setUserName("Akash");
            userCredentialDTO.setEmail("akash@example.com");
            userCredentialDTO.setPassword("password123");
            userCredentialDTO.setRole("USER");

            UserCredential userCredential = new UserCredential();
            userCredential.setPassword("password123");

            UserCredential savedUserCredential = new UserCredential();
            savedUserCredential.setId(1);

            UserCredentialDTO savedUserCredentialDTO = new UserCredentialDTO();
            savedUserCredentialDTO.setId(1);

            when(modelMapper.map(any(UserCredentialDTO.class), eq(UserCredential.class))).thenReturn(userCredential);
            when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
            when(userCredentialRepository.save(any(UserCredential.class))).thenReturn(savedUserCredential);
            when(modelMapper.map(any(UserCredential.class), eq(UserCredentialDTO.class))).thenReturn(savedUserCredentialDTO);

            authServiceImpl.saveUser(userCredentialDTO);
            assertEquals("encodedPassword", userCredential.getPassword());
        } catch (Exception ex) {
            assertTrue(false);
        }
    }

    @Test
    void testSaveUserNegativeWhenUserNameIsBlank() {
        UserCredentialDTO userCredentialDTO = new UserCredentialDTO();
        userCredentialDTO.setUserName("");
        userCredentialDTO.setEmail("yash@example.com");
        userCredentialDTO.setPassword("password123");

        Set<ConstraintViolation<UserCredentialDTO>> violations = validator.validate(userCredentialDTO);

        assertThat(violations)
                .extracting(v -> v.getMessage())
                .anyMatch(msg -> msg.contains("User Name cannot be blank"));
    }

    @Test
    void testSaveUserNegativeWhenUserNameLengthIsLess() {
        UserCredentialDTO userCredentialDTO = new UserCredentialDTO();
        userCredentialDTO.setUserName("ya");
        userCredentialDTO.setEmail("yash@example.com");
        userCredentialDTO.setPassword("password123");

        Set<ConstraintViolation<UserCredentialDTO>> violations = validator.validate(userCredentialDTO);

        assertThat(violations)
                .extracting(v -> v.getMessage())
                .anyMatch(msg -> msg.contains("User Name must be between 3 to 50 characters"));
    }

    @Test
    void testSaveUserNegativeWhenEmailIsBlank() {
        UserCredentialDTO userCredentialDTO = new UserCredentialDTO();
        userCredentialDTO.setUserName("Yash");
        userCredentialDTO.setEmail("");
        userCredentialDTO.setPassword("password123");

        Set<ConstraintViolation<UserCredentialDTO>> violations = validator.validate(userCredentialDTO);

        assertThat(violations)
                .extracting(v -> v.getMessage())
                .anyMatch(msg -> msg.contains("Email cannot be blank"));
    }

    @Test
    void testSaveUserNegativeWhenEmailIsInvalid() {
        UserCredentialDTO userCredentialDTO = new UserCredentialDTO();
        userCredentialDTO.setUserName("Yash");
        userCredentialDTO.setEmail("yashexamplecom");
        userCredentialDTO.setPassword("password123");

        Set<ConstraintViolation<UserCredentialDTO>> violations = validator.validate(userCredentialDTO);

        assertThat(violations)
                .extracting(v -> v.getMessage())
                .anyMatch(msg -> msg.contains("Please enter a valid email"));
    }

    @Test
    void testGenerateTokenPositive() {
        try {
            when(jwtService.generateToken("Suraj", "USER")).thenReturn("dummy-jwt-token");

            String actualToken = authServiceImpl.generateToken("Suraj", "USER");
            assertNotNull(actualToken);
        } catch (Exception ex) {
            assertTrue(false);
        }
    }

    @Test
    void testGenerateTokenPositiveAssertValue() {
        try {
            when(jwtService.generateToken("Suraj", "USER")).thenReturn("dummy-jwt-token");

            String actualToken = authServiceImpl.generateToken("Suraj", "USER");
            assertEquals("dummy-jwt-token", actualToken);
        } catch (Exception ex) {
            assertTrue(false);
        }
    }

    @Test
    void testGenerateTokenNegativeWhenJwtServiceReturnsNull() {
        try {
            when(jwtService.generateToken(anyString(), anyString())).thenReturn(null);

            String actualToken = authServiceImpl.generateToken("Suraj", "USER");
            assertNull(actualToken);
        } catch (Exception ex) {
            assertTrue(false);
        }
    }

    @Test
    void testValidateTokenPositive() {
        try {
            TokenValidationResponse tokenValidationResponse = new TokenValidationResponse(true, "Suraj", "USER");
            when(jwtService.validateToken("dummy-jwt-token")).thenReturn(tokenValidationResponse);

            TokenValidationResponse actualResponse = authServiceImpl.validateToken("dummy-jwt-token");
            assertNotNull(actualResponse);
        } catch (Exception ex) {
            assertTrue(false);
        }
    }

    @Test
    void testValidateTokenPositiveAssertUserName() {
        try {
            TokenValidationResponse tokenValidationResponse = new TokenValidationResponse(true, "Suraj", "USER");
            when(jwtService.validateToken("dummy-jwt-token")).thenReturn(tokenValidationResponse);

            TokenValidationResponse actualResponse = authServiceImpl.validateToken("dummy-jwt-token");
            assertEquals("Suraj", actualResponse.getUserName());
        } catch (Exception ex) {
            assertTrue(false);
        }
    }

    @Test
    void testValidateTokenNegativeWhenTokenIsInvalid() {
        try {
            when(jwtService.validateToken(anyString())).thenThrow(new RuntimeException("Invalid token"));
            authServiceImpl.validateToken("invalid-token");
        } catch (Exception ex) {
            assertThat(ex)
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Invalid token");
        }
    }

    // ---- Rate limiter fallbacks ----

    @Test
    void testSaveUserFallbackThrowsRateLimitExceeded() {
        RequestNotPermitted ex = mock(RequestNotPermitted.class);
        UserCredentialDTO userCredentialDTO = new UserCredentialDTO();

        RateLimitExceededException thrown = assertThrows(
                RateLimitExceededException.class,
                () -> authServiceImpl.saveUserFallback(userCredentialDTO, ex));

        assertEquals("Too many requests. Please try again later.", thrown.getMessage());
    }

    @Test
    void testGenerateTokenFallbackThrowsRateLimitExceeded() {
        RequestNotPermitted ex = mock(RequestNotPermitted.class);

        RateLimitExceededException thrown = assertThrows(
                RateLimitExceededException.class,
                () -> authServiceImpl.generateTokenFallback("Suraj", "USER", ex));

        assertEquals("Too many requests. Please try again later.", thrown.getMessage());
    }

    @Test
    void testValidateTokenFallbackThrowsRateLimitExceeded() {
        RequestNotPermitted ex = mock(RequestNotPermitted.class);

        RateLimitExceededException thrown = assertThrows(
                RateLimitExceededException.class,
                () -> authServiceImpl.validateTokenFallback("dummy-jwt-token", ex));

        assertEquals("Too many requests. Please try again later.", thrown.getMessage());
    }
}
