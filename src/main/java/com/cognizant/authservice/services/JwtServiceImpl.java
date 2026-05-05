package com.cognizant.authservice.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtServiceImpl implements JwtService{
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Override
    public String generateToken(String userName) {
        Map<String, Object> claims=new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

//    @Override
//    public String createToken(Map<String, Object> claims, String userName) {
//        return Jwts.builder()
//            .setClaims(claims)
//            .setSubject(userName)
//            .setIssuedAt(new Date(System.currentTimeMillis()))
//            .setExpiration(new Date(System.currentTimeMillis() + 1000*60*30))
//            .signWith(getSignKey(), SignatureAlgorithm.HS256)
//            .compact();
//    }

    @Override
    public void validateToken(String token) {
        Jws<Claims> claimsJws=Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }

    @Override
    public Key getSignKey() {
        byte[]  keyBites= Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBites);
    }

}
