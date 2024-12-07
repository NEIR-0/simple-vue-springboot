//package com.restApi.RestAPI.config;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.exceptions.JWTVerificationException;
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//// jangan pakai @Configuration pakai @Component, some how lebih aman
//@Component
//public class JwtFilter implements Filter {
//    private final String secretKey = "rahasia";  // Sesuaikan dengan secret key
//    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//
//        // Ambil header Authorization
//        String authHeader = httpRequest.getHeader("Authorization");
//
//        // Cek apakah header Authorization ada dan dimulai dengan "Bearer "
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7); // Ambil token setelah "Bearer "
//            logger.info("Token received: {}", token);
//
//            try {
//                // Verifikasi token
//                Algorithm algorithm = Algorithm.HMAC256(secretKey);
//                JWT.require(algorithm).build().verify(token);
//            } catch (JWTVerificationException e) {
//                // Jika token tidak valid
//                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                httpResponse.getWriter().write("Invalid or expired token");
//                return;
//            }
//        } else {
//            // Jika tidak ada header Authorization
//            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            httpResponse.getWriter().write("Authorization header missing or invalid");
//            return;
//        }
//
//        // Lanjutkan ke filter berikutnya jika token valid
//        chain.doFilter(request, response);
//    }
//}