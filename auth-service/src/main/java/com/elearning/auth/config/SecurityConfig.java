package com.elearning.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // désactive CSRF pour Postman
            .authorizeHttpRequests(auth -> auth
                // endpoints publics
                .requestMatchers(
                    new AntPathRequestMatcher("/auth/register"),
                    new AntPathRequestMatcher("/auth/login")
                ).permitAll()
                // H2 console autorisée
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                // tout le reste nécessite un JWT
                .anyRequest().authenticated()
            )
            .headers(headers -> headers
                .defaultsDisabled() // désactive les headers par défaut
                .frameOptions(frame -> frame.sameOrigin()) // permet H2 console
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
