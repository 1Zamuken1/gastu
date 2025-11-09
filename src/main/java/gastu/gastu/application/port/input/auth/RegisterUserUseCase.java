package gastu.gastu.application.port.input.auth;

import gastu.gastu.infrastructure.adapter.input.rest.dto.request.auth.RegisterRequest;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.auth.UserResponse;

/**
 * Puerto de entrada para el caso de uso de Registro de Usuario
 */
public interface RegisterUserUseCase {

    /**
     * Registra un nuevo usuario en el sistema
     *
     * @param request Datos del usuario a registrar
     * @return UserResponse con los datos del usuario creado
     * @throws gastu.gastu.domain.exception.BusinessException si el correo o teléfono ya existen
     */
    UserResponse execute(RegisterRequest request);
}