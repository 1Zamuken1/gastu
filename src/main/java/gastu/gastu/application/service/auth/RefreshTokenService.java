package gastu.gastu.application.service.auth;

import gastu.gastu.application.port.input.auth.RefreshTokenUseCase;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.auth.AuthResponse;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.auth.UserResponse;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.UsuarioEntity;
import gastu.gastu.infrastructure.adapter.output.persistence.repository.JpaUsuarioRepository;
import gastu.gastu.infrastructure.config.security.JwtTokenProvider;
import gastu.gastu.infrastructure.config.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para refrescar tokens JWT
 * Implementa el caso de uso RefreshTokenUseCase
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenService implements RefreshTokenUseCase {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsService;
    private final JpaUsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public AuthResponse execute(String refreshToken) {
        // Validar el token
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Refresh token inválido o expirado");
        }

        // Extraer el username (correo) del token
        String correo = jwtTokenProvider.getUsernameFromToken(refreshToken);

        // Cargar el usuario
        UserDetails userDetails = userDetailsService.loadUserByUsername(correo);

        // Crear autenticación
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        // Generar nuevo access token
        String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);

        // Obtener información del usuario
        UsuarioEntity usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        UserResponse userResponse = UserResponse.builder()
                .usuarioId(usuario.getUsuarioId())
                .nombre(usuario.getNombre())
                .correo(usuario.getCorreo())
                .telefono(usuario.getTelefono())
                .rol(usuario.getRol().getNombre())
                .activo(usuario.isActivo())
                .fechaRegistro(usuario.getFechaRegistro())
                .build();

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken) // Se mantiene el mismo refresh token
                .tokenType("Bearer")
                .usuario(userResponse)
                .build();
    }
}