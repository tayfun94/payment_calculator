package ro.tayfy.payment.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    private long jwtExpirationInMs = 3_600_000; // 1 hour in milliseconds

    public String generateToken(String username) {
        Instant now = Instant.now();
        Instant expiration = now.plusMillis(jwtExpirationInMs);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        return getJwtBody(token).getSubject();
    }

    private Claims getJwtBody(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        Instant expiration = getJwtBody(token).getExpiration().toInstant();
        return expiration.isBefore(Instant.now());
    }

    public Instant getTokenExpiration(String token) {
        return getJwtBody(token).getExpiration().toInstant();
    }
}