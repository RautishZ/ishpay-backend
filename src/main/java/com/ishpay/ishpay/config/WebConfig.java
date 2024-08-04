package com.ishpay.ishpay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("https://ishpay.com") // Remove trailing slash
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // Restrict methods
                        .allowedHeaders("Authorization", "Content-Type", "X-Requested-With") // Specify necessary headers
                        .allowCredentials(true);
            }
        };
    }
}
