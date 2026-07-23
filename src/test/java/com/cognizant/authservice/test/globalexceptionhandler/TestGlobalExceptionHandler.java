package com.cognizant.authservice.test.globalexceptionhandler;

import com.cognizant.authservice.dtos.ErrorResponse;
import com.cognizant.authservice.globalexceptionhandler.GlobalExceptionHandler;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TestGlobalExceptionHandler {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() throws Exception {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Test
    void handleUsernameNotFoundException_returns404WithMessage() {
        UsernameNotFoundException ex = new UsernameNotFoundException("User not found with name: Suraj");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleUsernameNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found with name: Suraj", response.getBody().getMessage());
    }

    @Test
    void handleBadCredentialsException_returns401WithGenericMessage() {
        BadCredentialsException ex = new BadCredentialsException("Bad credentials detail");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleBadCredentialsException(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid username or password", response.getBody().getMessage());
    }

    @Test
    void handleExpiredJwtException_returns401WithGenericMessage() {
        ExpiredJwtException ex = mock(ExpiredJwtException.class);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleExpiredJwtException(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Token has expired", response.getBody().getMessage());
    }

    @Test
    void handleJwtException_returns401WithGenericMessage() {
        JwtException ex = mock(JwtException.class);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleJwtException(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid token", response.getBody().getMessage());
    }

    @Test
    void handleValidationException_returns400WithJoinedMessages() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError error1 = new FieldError("userCredentialDTO", "userName", "User Name cannot be blank");
        FieldError error2 = new FieldError("userCredentialDTO", "email", "Please enter a valid email");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(error1, error2));
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User Name cannot be blank, Please enter a valid email", response.getBody().getMessage());
    }

    @Test
    void handleValidationException_singleError_returnsMessage() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError error = new FieldError("authRequest", "password", "Password cannot be blank");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(error));
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Password cannot be blank", response.getBody().getMessage());
    }

    @Test
    void handleGenericException_returns500WithGenericMessage() {
        Exception ex = new RuntimeException("Something exploded");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGenericException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred", response.getBody().getMessage());
    }

    @Test
    void handleUsernameNotFoundException_assertStatusValue() {
        UsernameNotFoundException ex = new UsernameNotFoundException("User not found with name: Akash");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleUsernameNotFoundException(ex);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void handleGenericException_assertStatusValue() {
        Exception ex = new RuntimeException("Boom");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGenericException(ex);

        assertEquals(500, response.getStatusCode().value());
    }
}
