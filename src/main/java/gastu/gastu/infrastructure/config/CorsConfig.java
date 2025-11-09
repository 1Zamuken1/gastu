package gastu.gastu.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Configuración de CORS para permitir peticiones desde frontend
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Orígenes permitidos (ajustar según necesidad)
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",  // React
                "http://localhost:4200",  // Angular
                "http://localhost:5173"   // Vite
        ));

        // Métodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));

        // Headers permitidos
        configuration.setAllowedHeaders(List.of("*"));

        // Permitir credenciales
        configuration.setAllowCredentials(true);

        // Tiempo de cache de la configuración CORS
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}