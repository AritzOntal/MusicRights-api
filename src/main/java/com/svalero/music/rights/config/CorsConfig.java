package com.svalero.music.rights.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Configuración CORS para permitir las peticiones del frontend (MusicRightsWeb)
 * al backend.
 *
 * Los orígenes permitidos se leen de la propiedad `musicrights.cors.allowed-origins`
 * (configurable por entorno en application-*.properties o como variable de entorno).
 * Si no se define, se permiten los orígenes locales de Vite (5173) y su preview (4173).
 */
@Configuration
public class CorsConfig {

    @Value("${musicrights.cors.allowed-origins:http://localhost:5173,http://localhost:4173,http://127.0.0.1:5173,http://127.0.0.1:4173}")
    private String[] allowedOrigins;

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Orígenes permitidos (frontend)
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins));

        // Métodos permitidos (incluye OPTIONS para el preflight)
        configuration.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD"
        ));

        // Cabeceras permitidas en la request
        configuration.setAllowedHeaders(List.of("*"));

        // Cabeceras que el navegador puede leer en la respuesta
        configuration.setExposedHeaders(List.of("Authorization", "Content-Type", "Location"));

        // No usamos cookies (el JWT viaja en Authorization), por lo que no es necesario.
        // Si en el futuro se usaran cookies, poner a true y ajustar orígenes.
        configuration.setAllowCredentials(false);

        // Cachear la respuesta del preflight 1 hora
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
