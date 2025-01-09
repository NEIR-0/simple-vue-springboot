package com.restApi.RestAPI.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${base.url.fe}")
    private String baseUrl;

    @Autowired
    private JwtFilter jwtFilter;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Mengizinkan semua path URL
                .allowedOrigins(baseUrl) // Mengizinkan hanya dari origin ini
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Mengizinkan metode HTTP tertentu
                .allowedHeaders("Authorization", "Content-Type", "Accept") // Mengizinkan semua header
                .allowCredentials(true)
                .exposedHeaders("Authorization");
    }

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilterRegistration() {
        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtFilter);
        registrationBean.addUrlPatterns("/users/*", "/products/*", "/message/*", "/token/*"); // Terapkan hanya untuk path tertentu
        return registrationBean;
    }
}
