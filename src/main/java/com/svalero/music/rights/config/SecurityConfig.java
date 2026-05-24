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
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    static {
        System.out.println("********** DEBUG: JVM ha cargado la clase SecurityConfig **********");
    }

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter,
                          CorsConfigurationSource corsConfigurationSource) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println(">>> EJECUTANDO: Configuración de seguridad aplicada correctamente <<<");

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // PREFLIGHT CORS (OPTIONS) siempre permitido
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // PÚBLICAS
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
                        .requestMatchers("/error", "/error/**").permitAll()
                        .requestMatchers("/api/v1/users/**").permitAll()

                        // Un usuario logueado puede convertirse a sí mismo en músico
                        .requestMatchers(HttpMethod.POST, "/api/v1/musicians/me").hasAnyRole("USER", "MUSICIAN")

                        // LECTURA (GET)
                        .requestMatchers(HttpMethod.GET, "/api/v1/works/**", "/api/v1/works").hasAnyRole("USER", "MUSICIAN", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/musicians/**", "/api/v1/musicians").hasAnyRole("USER", "MUSICIAN", "ADMIN")


                        // (POST, PUT, DELETE)
                        .requestMatchers("/api/v1/works/**").hasAnyRole("MUSICIAN", "ADMIN")
                        .requestMatchers("/api/v1/claims/**").hasAnyRole("MUSICIAN", "ADMIN")
                        .requestMatchers("/api/v1/concerts/**").hasAnyRole("MUSICIAN", "ADMIN")

                        // Lista de documentos del músico logueado
                        .requestMatchers(HttpMethod.GET, "/api/v1/documents/mine").hasAnyRole("MUSICIAN", "ADMIN")

                        // Borrado de un documento propio
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/documents/mine/*").hasAnyRole("MUSICIAN", "ADMIN")

                        // Descarga de documentos generados (URL prefirmada): músico o admin
                        .requestMatchers(HttpMethod.GET, "/api/v1/documents/*/download").hasAnyRole("MUSICIAN", "ADMIN")

                        // RESTO
                        .anyRequest().hasRole("ADMIN")
                    )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
