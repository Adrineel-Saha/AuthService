package com.cognizant.authservice.test.config;

import com.cognizant.authservice.main.AuthServiceApplication;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = AuthServiceApplication.class)
@ActiveProfiles("test")
class TestAuthConfig {

    @Autowired
    private SecurityFilterChain securityFilterChain;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Test
    void securityBeansAreCreated() {
        assertNotNull(securityFilterChain);
        assertNotNull(authenticationEntryPoint);
        assertNotNull(passwordEncoder);
        assertNotNull(authenticationManager);
        assertNotNull(authenticationProvider);
    }

    @Test
    void passwordEncoderEncodesAndMatches() {
        String encoded = passwordEncoder.encode("password123");

        assertNotNull(encoded);
        assertTrue(passwordEncoder.matches("password123", encoded));
    }

    @Test
    void authenticationEntryPoint_sends401WithInvalidCredentials() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AuthenticationException authException = mock(AuthenticationException.class);

        authenticationEntryPoint.commence(request, response, authException);

        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid credentials");
    }
}
