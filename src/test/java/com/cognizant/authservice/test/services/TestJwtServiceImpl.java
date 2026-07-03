package com.cognizant.authservice.test.services;

import com.cognizant.authservice.dtos.TokenValidationResponse;
import com.cognizant.authservice.services.JwtServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;

import static org.junit.jupiter.api.Assertions.*;

class TestJwtServiceImpl {
    private static final String TEST_SECRET = "PJvubyowaL8Y5ki9mcvARME+QLzAzUFVWwGjCpRsNJc=";

    private JwtServiceImpl jwtServiceImpl;

    @BeforeEach
    void setUp() throws Exception {
        jwtServiceImpl = new JwtServiceImpl();
        ReflectionTestUtils.setField(jwtServiceImpl, "jwtSecret", TEST_SECRET);
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Test
    void testGenerateTokenPositive() {
        String token = jwtServiceImpl.generateToken("Suraj", "USER");
        assertNotNull(token);
    }

    @Test
    void testGenerateTokenPositiveAssertTokenHasThreeParts() {
        String token = jwtServiceImpl.generateToken("Suraj", "USER");
        assertEquals(3, token.split("\\.").length);
    }

    @Test
    void testGetSignKeyPositive() {
        Key key = jwtServiceImpl.getSignKey();
        assertNotNull(key);
    }

    @Test
    void testValidateTokenPositive() {
        String token = jwtServiceImpl.generateToken("Suraj", "USER");

        TokenValidationResponse response = jwtServiceImpl.validateToken(token);
        assertNotNull(response);
    }

    @Test
    void testValidateTokenPositiveAssertUserName() {
        String token = jwtServiceImpl.generateToken("Suraj", "USER");

        TokenValidationResponse response = jwtServiceImpl.validateToken(token);
        assertEquals("Suraj", response.getUserName());
    }

    @Test
    void testValidateTokenPositiveAssertRole() {
        String token = jwtServiceImpl.generateToken("Suraj", "ADMIN");

        TokenValidationResponse response = jwtServiceImpl.validateToken(token);
        assertEquals("ADMIN", response.getRole());
    }

    @Test
    void testValidateTokenPositiveAssertValidFlag() {
        String token = jwtServiceImpl.generateToken("Suraj", "USER");

        TokenValidationResponse response = jwtServiceImpl.validateToken(token);
        assertTrue(response.isValid());
    }

    @Test
    void testValidateTokenNegativeWhenTokenIsMalformed() {
        try {
            jwtServiceImpl.validateToken("not-a-valid-token");
            assertTrue(false);
        } catch (MalformedJwtException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    void testValidateTokenNegativeWhenTokenIsExpired() {
        JwtServiceImpl expiringJwtService = new JwtServiceImpl() {
            @Override
            public String generateToken(String userName, String role) {
                return io.jsonwebtoken.Jwts.builder()
                        .setSubject(userName)
                        .claim("role", role)
                        .setIssuedAt(new java.util.Date(System.currentTimeMillis() - 1000 * 60 * 60))
                        .setExpiration(new java.util.Date(System.currentTimeMillis() - 1000 * 60 * 30))
                        .signWith(getSignKey(), io.jsonwebtoken.SignatureAlgorithm.HS256)
                        .compact();
            }
        };
        ReflectionTestUtils.setField(expiringJwtService, "jwtSecret", TEST_SECRET);

        String expiredToken = expiringJwtService.generateToken("Suraj", "USER");

        try {
            expiringJwtService.validateToken(expiredToken);
            assertTrue(false);
        } catch (ExpiredJwtException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    void testValidateTokenNegativeWhenSignedWithDifferentKey() {
        JwtServiceImpl otherJwtService = new JwtServiceImpl();
        ReflectionTestUtils.setField(otherJwtService, "jwtSecret", "YEL8Vybv5mIVhXrFlK+gTnsmsw6kTAmwSAplpjpr3LI=");

        String token = otherJwtService.generateToken("Suraj", "USER");

        assertThrows(io.jsonwebtoken.security.SignatureException.class, () -> jwtServiceImpl.validateToken(token));
    }
}
