package com.project.project.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    private static final int FIFTEEN_MIN_AD_MILLIS = 120 * 60 * 1000;

    private final Environment environment;

    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(Objects.requireNonNull(environment.getProperty("SECRET_KEY")).getBytes()).parseClaimsJws(token).getBody();
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public String generateToken(String username) {

        Date tokenExpirationTime = new Date(System.currentTimeMillis() + FIFTEEN_MIN_AD_MILLIS);

        return Jwts.builder().setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(tokenExpirationTime)
                .signWith(SignatureAlgorithm.HS256, Objects.requireNonNull(environment.getProperty("SECRET_KEY")).getBytes()).compact();
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

}
