package com.mazemzemi.soldstack.auth.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Définit le bean pour l'encodage des mots de passe.
     * Ce bean sera injecté automatiquement dans votre AuthService.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configure la chaîne de filtres de sécurité HTTP.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Désactiver CSRF, car nous utilisons des tokens JWT (API stateless)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Définir les règles d'autorisation pour les requêtes HTTP
                .authorizeHttpRequests(authorize -> authorize
                        // Permettre l'accès public aux endpoints d'authentification
                        .requestMatchers("/api/auth/**").permitAll()
                        // Exiger une authentification pour toutes les autres requêtes
                        .anyRequest().authenticated()
                )

                // 3. Configurer la gestion de session pour qu'elle soit stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // À l'avenir, vous ajouterez ici votre filtre de validation JWT
        // http.addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}