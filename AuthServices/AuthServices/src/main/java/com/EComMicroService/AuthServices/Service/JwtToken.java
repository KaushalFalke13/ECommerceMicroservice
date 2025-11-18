package com.EComMicroService.AuthServices.Service;

import java.util.Date;

import org.springframework.stereotype.Service;

import java.security.Key;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;


@Service
public class JwtToken {

    private static final Key key = Keys.hmacShaKeyFor("supersecretkeysupersecretkey12345".getBytes());


    public static String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) 
                .signWith(key)
                .compact();
    }

     public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    
    public boolean validateToken(String token) {
    try {
        Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token); 
        return true;
    } catch (JwtException | IllegalArgumentException e) {
        return false;
    }
}

}
