package com.weather.weather_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Vypneme CSRF pro API a H2 konzoli
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // Povolíme přístup všem (případně si to pak můžeš zabezpečit klíčem)
            );
        return http.build();
    }
}