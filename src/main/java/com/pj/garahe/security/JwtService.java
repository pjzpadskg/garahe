package com.pj.garahe.security;

import com.pj.garahe.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final ObjectMapper objectMapper;
    private final JwtProperties jwtProperties;

    private SecretKey getKey() {
        byte[] bytes = Decoders.BASE64.decode(jwtProperties.secret());
        return Keys.hmacShaKeyFor(bytes);
    }

    public String generateToken(JwtExtraClaims extra, UserDetails userDetails) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .claims(objectMapper.convertValue(extra, new TypeReference<Map<String, Object>>() {}))
                .subject(userDetails.getUsername())
                .issuedAt(new Date(now))
                .expiration(new Date(now + jwtProperties.expiration()))
                .signWith(getKey())
                .compact();
    }

    private Claims extract(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public JwtExtraClaims extractExtra(String token) {
        return objectMapper.convertValue(extract(token), JwtExtraClaims.class);
    }

    public String getEmail(String token) {
        return extract(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        try {
            return extract(token).getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            String emailFromToken = getEmail(token);
            return emailFromToken.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}
