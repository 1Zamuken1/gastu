package gastu.gastu.application.service.auth;

import gastu.gastu.application.port.input.auth.LoginUseCase;
import gastu.gastu.infrastructure.adapter.input.rest.dto.request.auth.LoginRequest;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.auth.AuthResponse;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.auth.UserResponse;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.UsuarioEntity;
import gastu.gastu.infrastructure.adapter.output.persistence.repository.JpaUsuarioRepository;
import gastu.gastu.infrastructure.config.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para manejar el login de usuarios
 * Implementa el caso de uso LoginUseCase
 */
@Service
@RequiredArgsConstructor
public class LoginService implements LoginUseCase {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final JpaUsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    @Override
    public AuthResponse execute(LoginRequest request) {
        // Autenticar usuario
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getCorreo(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generar tokens
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        // Obtener información del usuario
        UsuarioEntity usuario = usuarioRepository.findByCorreo(request.getCorreo())
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
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .usuario(userResponse)
                .build();
    }
}