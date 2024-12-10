package com.restApi.RestAPI.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class JwtUtil {

    private final String secretKey = "{jwt.secret}";

    public String generateToken(String email, Long id) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.create()
                .withSubject(email)
                .withClaim("id", id)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60))  // Token expires in 1 hour
                .sign(algorithm);
    }

    public DecodedJWT verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.require(algorithm).build().verify(token);
    }

    public String getEmailFromToken(String token) {
        return verifyToken(token).getSubject();
    }

    public Long getIdFromToken(String token) {
        return verifyToken(token).getClaim("id").asLong();
    }
}