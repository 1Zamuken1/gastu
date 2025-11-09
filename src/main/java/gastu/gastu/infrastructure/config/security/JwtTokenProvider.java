package gastu.gastu.infrastructure.config.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Componente responsable de generar y validar tokens JWT
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    /**
     * Genera la clave secreta a partir del string configurado
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Genera un access token JWT
     */
    public String generateAccessToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return generateToken(userDetails, jwtProperties.getExpiration());
    }

    /**
     * Genera un refresh token JWT
     */
    public String generateRefreshToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return generateToken(userDetails, jwtProperties.getRefreshExpiration());
    }

    /**
     * Genera un token JWT con la expiración especificada
     */
    private String generateToken(UserDetails userDetails, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        // Extraer roles/authorities
        String roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .subject(userDetails.getUsername()) // Email del usuario
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extrae el username (correo) del token
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    /**
     * Valida si el token es válido
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Token JWT mal formado: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("Token JWT expirado: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Token JWT no soportado: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("Claims del JWT vacío: {}", ex.getMessage());
        } catch (Exception ex) {
            log.error("Error validando token JWT: {}", ex.getMessage());
        }
        return false;
    }
}