package com.example.demo.config;

import com.example.demo.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // IMPORTANTE: Activa @PreAuthorize en los Controllers
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 1. PUBLICO: Login, registro y ver el mapa/mascotas
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/usuarios/registro").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/publicaciones/mapa").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/mascotas").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/mascotas/buscar").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/mascotas/usuario").permitAll()

                        // 2. EXCLUSIVO ADMIN (Solo ejemplos de rutas globales de moderación)
                        // Si tenés un panel de admin, va acá.
                        // Si querés que el DELETE genérico sea solo de admin, podés hacer:
                        // .requestMatchers(HttpMethod.DELETE, "/api/usuarios/**").hasAuthority("ROLE_ADMIN")

                        // 3. AUTENTICADOS (USER o ADMIN)
                        // Cualquier otra petición (POST para crear, PUT para editar) requiere token.
                        // Si el usuario intenta editar algo que no es suyo, lo frena el @PreAuthorize en el Controller.
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
