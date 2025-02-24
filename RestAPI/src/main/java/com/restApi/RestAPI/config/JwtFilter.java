package com.restApi.RestAPI.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

// jangan pakai @Configuration pakai @Component, some how lebih aman
@Component
public class JwtFilter implements Filter {
    @Value("${base.url.fe}")
    private String baseUrl;

    @Autowired
    JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // wajib buat cors
        httpResponse.setHeader("Access-Control-Allow-Origin", baseUrl);
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpResponse.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
        httpResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");

        // validasi untuk get products
        String path = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();
        if ("/products".equals(path) && "GET".equalsIgnoreCase(method)) {
            chain.doFilter(request, response);
            return; // Lewatkan tanpa validasi
        }

        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // Ambil header Authorization
        String authHeader = httpRequest.getHeader("Authorization");
        System.out.println("token: " + authHeader);

        // Cek apakah header Authorization ada dan dimulai dengan "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String[] token = authHeader.split(" ");

            try {
                // Verifikasi token
                System.out.println("token: " + token[1] + ">>>>>>>>>>>");
                DecodedJWT decodedJWT = jwtUtil.verifyToken(token[1]);
                System.out.println("success verify >>>>>>>>>>>");

                //  decodedJWT.getClaims().forEach((key, value) -> {
                //      System.out.println(key + ": " + value.asString());
                //  });
                Long userId = decodedJWT.getClaim("id").asLong();
                String userRole = decodedJWT.getClaim("role").asString();
                request.setAttribute("userId", userId);
                request.setAttribute("userRole", userRole);
            } catch (JWTVerificationException e) {
                // Jika token tidak valid
                System.out.println("unsuccess verify >>>>>>>>>>>");
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
