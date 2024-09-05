package com.marcpinol.authservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtService {
    @Value("${com.marcpinol.jwt.secret}")
    private String SECRET;
    @Value("${com.marcpinol.jwt.valid-token-time}")
    private long validTokenTime;

    public boolean validateToken(String token) {
        getClaims(token);
        return true;
    }

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();

        return Jwts.builder()
                .subject(username)
                .claim("authorities", authentication.getAuthorities())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + validTokenTime))
                .signWith(getKey())
                .compact();
    }

    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    private Key getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
