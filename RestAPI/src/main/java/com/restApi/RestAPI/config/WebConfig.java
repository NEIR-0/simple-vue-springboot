package com.restApi.RestAPI.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

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
                .allowedOrigins("http://localhost:5173") // Mengizinkan hanya dari origin ini
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Mengizinkan metode HTTP tertentu
                .allowedHeaders("Authorization", "Content-Type", "Accept") // Mengizinkan semua header
                .allowCredentials(true)
                .exposedHeaders("Authorization");
    }

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilterRegistration() {
        FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtFilter);
        registrationBean.addUrlPatterns("/users/*", "/products/*", "/message/*"); // Terapkan hanya untuk path tertentu
        return registrationBean;
    }
}
