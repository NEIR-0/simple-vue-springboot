package com.restApi.RestAPI.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

// jangan pakai @Configuration pakai @Component, some how lebih aman
@Component
public class JwtFilter implements Filter {
    private final String secretKey = "{jwt.secret}";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // wajib buat cors
        httpResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpResponse.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
        httpResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");

        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        String test = httpRequest.getHeader("Authorization");

        // Ambil header Authorization
        String authHeader = httpRequest.getHeader("Authorization");
        System.out.println("token: " + authHeader);

        // Cek apakah header Authorization ada dan dimulai dengan "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String[] token = authHeader.split(" ");

            try {
                // Verifikasi token
                Algorithm algorithm = Algorithm.HMAC256(secretKey);
                System.out.println("secretKey: " + secretKey + ">>>>>>>>>>>");
                System.out.println("token: " + token[1] + ">>>>>>>>>>>");
                JWT.require(algorithm).build().verify(token[1]);
            } catch (JWTVerificationException e) {
                // Jika token tidak valid
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.getWriter().write("Invalid or expired token");
                return;
            }
        } else {
            // Jika tidak ada header Authorization
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("Authorization header missing or invalid");
            return;
        }

        // Lanjutkan ke filter berikutnya jika token valid
        chain.doFilter(request, response);
    }
}
