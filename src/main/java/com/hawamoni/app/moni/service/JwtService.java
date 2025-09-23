package com.hawamoni.app.moni.service;

import com.hawamoni.app.moni.dto.UserDTO;
import com.hawamoni.app.moni.tokens.AccessToken;
import com.hawamoni.app.moni.tokens.RefreshToken;
import com.hawamoni.app.moni.tokens.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

    private String encodedKey;
    private SecretKey key = null;

    public JwtService() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            if(key == null) {
                key = keyGenerator.generateKey();
                encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    public AccessToken generateAccessKey(UserDTO userDTO) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type",TokenType.ACCESS.name());
        claims.put("userId",userDTO.getId());

        long exp_in_millis = 5 * 60 * 1000;
        Date expiration_time = new Date(System.currentTimeMillis() + exp_in_millis);
        String token =  Jwts.builder()
                .claims(claims)
                .subject(userDTO.getEmail())
                .expiration(expiration_time)
                .issuedAt(new Date(System.currentTimeMillis()))
                .signWith(getKey())
                .compact();

        return AccessToken.builder()
                .access_token(token)
                .access_expiry_time(expiration_time)
                .build();
    }

    public String getTokenType(String token) {
        return extractClaims(token).get("type").toString();
    }

    public RefreshToken generateRefreshToken(UserDTO userDTO) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", TokenType.REFRESH.name());
        claims.put("userId",userDTO.getId());

        long exp_in_millis = 7 * 24 * 60 * 60 * 1000;
        Date expiration_time = new Date(System.currentTimeMillis() + exp_in_millis);
        String token =  Jwts.builder()
                .claims(claims)
                .subject(userDTO.getEmail())
                .expiration(expiration_time)
                .issuedAt(new Date(System.currentTimeMillis()))
                .signWith(getKey())
                .compact();

        return RefreshToken.builder()
                .refresh_token(token)
                .refresh_expiry_time(expiration_time)
                .build();
    }

    public SecretKey getKey() {
        byte[] decodeKey = Base64.getDecoder().decode(encodedKey);
        return Keys.hmacShaKeyFor(decodeKey);
    }
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenExpired(String token) {
        return new Date(System.currentTimeMillis()).after(getExpiration(token));
    }

    public <T>T extractClaim(String token, Function<Claims,T> claimsResolver) {
        Claims claims  = extractClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractEmail(String token) {
        return extractClaim(token,Claims::getSubject);
    }

    public Date getExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
      String email = extractEmail(token);
      return ((Objects.equals(email,userDetails.getUsername())) && !isTokenExpired(token));
    }
}
