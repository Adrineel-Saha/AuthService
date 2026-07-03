package com.cognizant.authservice.test.config;

import com.cognizant.authservice.config.CustomUserDetailsService;
import com.cognizant.authservice.entities.UserCredential;
import com.cognizant.authservice.repositories.UserCredentialRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TestCustomUserDetailsService {
    @Mock
    private UserCredentialRepository userCredentialRepository;
    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Test
    void testLoadUserByUsernamePositive() {
        UserCredential userCredential = new UserCredential();
        userCredential.setUserName("Suraj");
        userCredential.setEmail("suraj@example.com");
        userCredential.setPassword("encodedPassword");
        userCredential.setRole("USER");

        when(userCredentialRepository.findByUserName("Suraj")).thenReturn(Optional.of(userCredential));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("Suraj");
        assertNotNull(userDetails);
    }

    @Test
    void testLoadUserByUsernamePositiveAssertUserName() {
        UserCredential userCredential = new UserCredential();
        userCredential.setUserName("Suraj");
        userCredential.setEmail("suraj@example.com");
        userCredential.setPassword("encodedPassword");
        userCredential.setRole("USER");

        when(userCredentialRepository.findByUserName("Suraj")).thenReturn(Optional.of(userCredential));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("Suraj");
        assertEquals("Suraj", userDetails.getUsername());
    }

    @Test
    void testLoadUserByUsernamePositiveAssertAuthorityRole() {
        UserCredential userCredential = new UserCredential();
        userCredential.setUserName("Akash");
        userCredential.setEmail("akash@example.com");
        userCredential.setPassword("encodedPassword");
        userCredential.setRole("ADMIN");

        when(userCredentialRepository.findByUserName("Akash")).thenReturn(Optional.of(userCredential));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("Akash");
        assertThat(userDetails.getAuthorities())
                .extracting(authority -> authority.getAuthority())
                .anyMatch(authority -> authority.equals("ROLE_ADMIN"));
    }

    @Test
    void testLoadUserByUsernameNegativeWhenUserNotFound() {
        when(userCredentialRepository.findByUserName(any())).thenReturn(Optional.empty());

        try {
            customUserDetailsService.loadUserByUsername("NonExistentUser");
            assertTrue(false);
        } catch (RuntimeException ex) {
            assertThat(ex).hasMessageContaining("User not found with name: NonExistentUser");
        }
    }
}
