package gastu.gastu.application.port.input.auth;

import gastu.gastu.infrastructure.adapter.input.rest.dto.request.auth.LoginRequest;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.auth.AuthResponse;

/**
 * Puerto de entrada para el caso de uso de Login
 */
public interface LoginUseCase {

    /**
     * Autentica un usuario y retorna tokens JWT
     *
     * @param request Datos de login (correo y password)
     * @return AuthResponse con tokens y datos del usuario
     * @throws org.springframework.security.authentication.BadCredentialsException si las credenciales son inválidas
     */
    AuthResponse execute(LoginRequest request);
}