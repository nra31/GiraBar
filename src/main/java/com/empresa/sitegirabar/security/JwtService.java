package com.empresa.sitegirabar.security;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {

    private static final String SECRET = "c25ea55d6f2f9835b0359d10f13fd61b388d56615b094b04b4b0f0a5273b157d";
    private static final Duration EXPIRATION_DURATION = Duration.ofMinutes(60);
    
    private final SecretKey signingKey;
    
    public JwtService() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }
    
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
            .verifyWith(signingKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject().equals(userDetails.getUsername()) && 
               claims.getExpiration().after(new Date());
    }

    public String generateToken(String username) {
        Instant now = Instant.now();
        return Jwts.builder()
            .subject(username)
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plus(EXPIRATION_DURATION)))
            .signWith(signingKey)
            .compact();
    }
    
}
