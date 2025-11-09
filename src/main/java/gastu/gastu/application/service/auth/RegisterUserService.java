package gastu.gastu.application.service.auth;

import gastu.gastu.application.port.input.auth.RegisterUserUseCase;
import gastu.gastu.domain.exception.BusinessException;
import gastu.gastu.infrastructure.adapter.input.rest.dto.request.auth.RegisterRequest;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.auth.UserResponse;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.RolEntity;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.UsuarioEntity;
import gastu.gastu.infrastructure.adapter.output.persistence.repository.JpaRolRepository;
import gastu.gastu.infrastructure.adapter.output.persistence.repository.JpaUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para registrar nuevos usuarios
 * Implementa el caso de uso RegisterUserUseCase
 */
@Service
@RequiredArgsConstructor
public class RegisterUserService implements RegisterUserUseCase {

    private final JpaUsuarioRepository usuarioRepository;
    private final JpaRolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse execute(RegisterRequest request) {
        // Validar que el correo no esté registrado
        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new BusinessException("El correo ya está registrado");
        }

        // Validar que el teléfono no esté registrado (si se proporcionó)
        if (request.getTelefono() != null && usuarioRepository.existsByTelefono(request.getTelefono())) {
            throw new BusinessException("El teléfono ya está registrado");
        }

        // Buscar el rol
        RolEntity rol = rolRepository.findByNombre(request.getRol())
                .orElseThrow(() -> new BusinessException("Rol no encontrado: " + request.getRol()));

        // Crear el usuario
        UsuarioEntity usuario = UsuarioEntity.builder()
                .nombre(request.getNombre())
                .correo(request.getCorreo())
                .telefono(request.getTelefono())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol(rol)
                .activo(true)
                .build();

        UsuarioEntity usuarioGuardado = usuarioRepository.save(usuario);

        return UserResponse.builder()
                .usuarioId(usuarioGuardado.getUsuarioId())
                .nombre(usuarioGuardado.getNombre())
                .correo(usuarioGuardado.getCorreo())
                .telefono(usuarioGuardado.getTelefono())
                .rol(usuarioGuardado.getRol().getNombre())
                .activo(usuarioGuardado.isActivo())
                .fechaRegistro(usuarioGuardado.getFechaRegistro())
                .build();
    }
}