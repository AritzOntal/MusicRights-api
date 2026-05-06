package com.svalero.music.rights.config;

import com.svalero.music.rights.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    static {
        System.out.println("********** DEBUG: JVM ha cargado la clase SecurityConfig **********");
    }

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println(">>> EJECUTANDO: Configuración de seguridad aplicada correctamente <<<");

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // PÚBLICAS
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
                        .requestMatchers("/error", "/error/**").permitAll()
                        .requestMatchers("/api/v1/users/**").permitAll()

                        // LECTURA (GET)
                        .requestMatchers(HttpMethod.GET, "/api/v1/works/**", "/api/v1/works").hasAnyRole("USER", "MUSICIAN", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/musicians/**", "/api/v1/musicians").hasAnyRole("USER", "MUSICIAN", "ADMIN")


                        // (POST, PUT, DELETE)
                        .requestMatchers("/api/v1/works/**").hasAnyRole("MUSICIAN", "ADMIN")
                        .requestMatchers("/api/v1/claims/**").hasAnyRole("MUSICIAN", "ADMIN")
                        .requestMatchers("/api/v1/concerts/**").hasAnyRole("MUSICIAN", "ADMIN")

                        // RESTO
                        .anyRequest().hasRole("ADMIN")
                    )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
