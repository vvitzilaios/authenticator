package com.sneakysquid.authenticator.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@UtilityClass
public class JwtUtil {

    private static final String SECRET = "AA649569B9753DEORESTIS4C7961557A4";

    public String extractUsername(String token) {
        return extractClaim(sanitizeToken(token), Claims::getSubject);
    }

    @SuppressWarnings("unchecked")
    public List<String> extractAuthorities(String token) {
        Claims claims = extractClaims(sanitizeToken(token));
        return claims.get("authorities", List.class);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractClaims(sanitizeToken(token));
        return claimsResolver.apply(claims);
    }

    public Claims extractClaims(String token) {
        String sanitizedToken = sanitizeToken(token);
        checkBase64UrlFormat(sanitizedToken);
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build().parseClaimsJws(sanitizedToken)
                .getBody();
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date(System.currentTimeMillis()));
    }

    private Date extractExpiration(String token) {
        return extractClaim(sanitizeToken(token), Claims::getExpiration);
    }

    private Key getSignInKey() {
        byte[] keyBytes = SECRET.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Sanitizes the token by trimming spaces and removing the "Bearer " prefix (if present).
     */
    private String sanitizeToken(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Invalid or empty JWT token.");
        }
        return token.replace("Bearer ", "").trim();
    }

    /**
     * Checks if the given token is properly Base64 URL encoded.
     */
    private void checkBase64UrlFormat(String token) {
        try {
            Base64.getUrlDecoder().decode(token.split("\\.")[0]); // Check header part
            Base64.getUrlDecoder().decode(token.split("\\.")[1]); // Check payload part
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid Base64 URL format in JWT token: " + ex.getMessage(), ex);
        }
    }
}