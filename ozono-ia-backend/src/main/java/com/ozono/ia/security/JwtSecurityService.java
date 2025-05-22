package com.ozono.ia.security;

import com.ozono.ia.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@Slf4j
@Service
public class JwtSecurityService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Integer jwtExpiration;

    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user);
    }

    private String generateToken(Map<String, Object> claims, User user) {
        String token = Jwts.builder()
                .subject(user.getUsername())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .issuedAt(new Date(System.currentTimeMillis()))
                .id(UUID.nameUUIDFromBytes(user.getEmail().getBytes()).toString())
                .signWith(getSecretKey())
                .compact();
        return ("Bearer " + token);
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims getAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String getTokenSubject(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public Date getTokenExpiration(String toke) {
        return getClaim(toke, Claims::getExpiration);
    }

    public String getTokenId(String token) {
        return getClaim(token, Claims::getId);
    }

    public Date getTokenIssuerAt(String token) {
        return getClaim(token, Claims::getIssuedAt);
    }

    public boolean isTokenExpired(String token) {
        return getTokenExpiration(token).before(new Date());
    }

    private boolean isTokenValid(String token, User user) {
        final String subject = getTokenSubject(token);
        return (subject.equals(user.getUsername()) && !isTokenExpired(token));
    }

    public Authentication validateToken(String token) {
        token = token.substring(7).trim();

        if (isTokenExpired(token)) {
            throw new MalformedJwtException("El token ha expirado");
        }

        final String carnet = getTokenSubject(token);
        return new UsernamePasswordAuthenticationToken(carnet, null, null);
    }


}



