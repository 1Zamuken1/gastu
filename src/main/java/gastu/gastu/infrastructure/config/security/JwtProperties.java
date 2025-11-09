package gastu.gastu.infrastructure.config.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Propiedades de configuración para JWT.
 * Lee valores desde application.yml bajo la clave 'jwt'
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * Clave secreta para firmar los tokens JWT
     * Mínimo 256 bits (32 caracteres)
     */
    private String secret;

    /**
     * Tiempo de expiración del access token en milisegundos
     * Por defecto: 86400000ms = 24 horas
     */
    private long expiration;

    /**
     * Tiempo de expiración del refresh token en milisegundos
     * Por defecto: 604800000ms = 7 días
     */
    private long refreshExpiration;
}