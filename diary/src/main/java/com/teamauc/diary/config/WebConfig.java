package com.teamauc.diary.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedOrigins("https://my-weather-diary.netlify.app/")
                .allowedOrigins("http://my-weather-diary.netlify.app/");
//                .allowedOrigins("http://localhost:3000")
//                .allowedOrigins("http://localhost:8080")
//                .allowedOrigins("http://localhost:80")
        ;
    }
}
