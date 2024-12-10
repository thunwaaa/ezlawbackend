package com.example.ezlawbackend.config;

import com.example.ezlawbackend.Auth.model.UserMySQL;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public UserMySQL userMySQL() {
        return new UserMySQL(); // Create a new instance if required
    }
}
