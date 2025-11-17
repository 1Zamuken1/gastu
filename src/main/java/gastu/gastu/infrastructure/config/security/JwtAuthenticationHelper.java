package gastu.gastu.infrastructure.config.security;

import gastu.gastu.infrastructure.adapter.output.persistence.entity.UsuarioEntity;
import gastu.gastu.infrastructure.adapter.output.persistence.repository.JpaUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Helper para extraer información del usuario autenticado desde el JWT
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationHelper {

    private final JpaUsuarioRepository usuarioRepository;

    /**
     * Extrae el ID del usuario autenticado desde el Authentication
     *
     * @param authentication Objeto de autenticación de Spring Security
     * @return ID del usuario autenticado
     * @throws UsernameNotFoundException si el usuario no existe en la BD
     */
    public Long getUserIdFromAuthentication(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("Usuario no autenticado");
        }

        // El username en nuestro caso es el correo
        String correo = authentication.getName();

        // Buscar el usuario por correo
        UsuarioEntity usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + correo));

        return usuario.getUsuarioId();
    }

    /**
     * Obtiene el correo del usuario autenticado
     *
     * @param authentication Objeto de autenticación de Spring Security
     * @return Correo del usuario autenticado
     */
    public String getCorreoFromAuthentication(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("Usuario no autenticado");
        }

        return authentication.getName();
    }
}