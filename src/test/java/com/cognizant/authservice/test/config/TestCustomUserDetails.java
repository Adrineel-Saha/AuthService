package com.cognizant.authservice.test.config;

import com.cognizant.authservice.config.CustomUserDetails;
import com.cognizant.authservice.entities.UserCredential;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestCustomUserDetails {

    private CustomUserDetails buildCustomUserDetails(String userName, String password, String role) {
        UserCredential userCredential = new UserCredential();
        userCredential.setUserName(userName);
        userCredential.setPassword(password);
        userCredential.setRole(role);
        return new CustomUserDetails(userCredential);
    }

    @Test
    void testGetUsername() {
        CustomUserDetails customUserDetails = buildCustomUserDetails("Suraj", "encodedPassword", "USER");
        assertEquals("Suraj", customUserDetails.getUsername());
    }

    @Test
    void testGetPassword() {
        CustomUserDetails customUserDetails = buildCustomUserDetails("Suraj", "encodedPassword", "USER");
        assertEquals("encodedPassword", customUserDetails.getPassword());
    }

    @Test
    void testGetAuthorities() {
        CustomUserDetails customUserDetails = buildCustomUserDetails("Akash", "encodedPassword", "ADMIN");
        assertTrue(customUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals("ROLE_ADMIN")));
    }

    @Test
    void testIsAccountNonExpired() {
        CustomUserDetails customUserDetails = buildCustomUserDetails("Suraj", "encodedPassword", "USER");
        assertTrue(customUserDetails.isAccountNonExpired());
    }

    @Test
    void testIsAccountNonLocked() {
        CustomUserDetails customUserDetails = buildCustomUserDetails("Suraj", "encodedPassword", "USER");
        assertTrue(customUserDetails.isAccountNonLocked());
    }

    @Test
    void testIsCredentialsNonExpired() {
        CustomUserDetails customUserDetails = buildCustomUserDetails("Suraj", "encodedPassword", "USER");
        assertTrue(customUserDetails.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled() {
        CustomUserDetails customUserDetails = buildCustomUserDetails("Suraj", "encodedPassword", "USER");
        assertTrue(customUserDetails.isEnabled());
    }
}
