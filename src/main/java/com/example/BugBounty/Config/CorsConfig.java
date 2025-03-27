package com.example.BugBounty.Config;

// ✅ Change "yourpackage" to your actual package name

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")  // ✅ Allows requests from all origins
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // ✅ Allowed HTTP methods
                        .allowedHeaders("*"); // ✅ Allows all headers
            }
        };
    }
}


