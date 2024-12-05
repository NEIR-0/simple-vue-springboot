package com.restApi.RestAPI.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class JwtUtil {

    private String secretKey = "rahasia";  // Secret key, should be stored securely

    public String generateToken(String email, Long id) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.create()
                .withSubject(email)
                .withClaim("id", id)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60))  // Token expires in 1 hour
                .sign(algorithm);
    }
}