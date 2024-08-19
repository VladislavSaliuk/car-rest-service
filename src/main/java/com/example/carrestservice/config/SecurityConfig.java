package com.example.carrestservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(registry -> {
                    registry
                            .requestMatchers(HttpMethod.GET, "/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/**").authenticated()
                            .requestMatchers(HttpMethod.PUT, "/**").authenticated()
                            .requestMatchers(HttpMethod.DELETE, "/**").authenticated()
                            .anyRequest().authenticated();
                })
                .csrf().disable()
                .oauth2ResourceServer(oauth2 -> oauth2.jwt())
                .build();
    }



}
