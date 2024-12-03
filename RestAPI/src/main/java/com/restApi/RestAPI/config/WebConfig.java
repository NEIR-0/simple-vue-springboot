package com.restApi.RestAPI.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Mengizinkan semua path URL
                .allowedOrigins("http://localhost:5173") // Mengizinkan hanya dari origin ini
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Mengizinkan metode HTTP tertentu
                .allowedHeaders("*"); // Mengizinkan semua header
    }
}
