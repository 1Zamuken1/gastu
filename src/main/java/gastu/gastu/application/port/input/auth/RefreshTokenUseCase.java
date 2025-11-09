package gastu.gastu.application.port.input.auth;

import gastu.gastu.infrastructure.adapter.input.rest.dto.response.auth.AuthResponse;

/**
 * Puerto de entrada para el caso de uso de Refresh Token
 */
public interface RefreshTokenUseCase {

    /**
     * Genera un nuevo access token usando un refresh token válido
     *
     * @param refreshToken Token de refresco válido
     * @return AuthResponse con el nuevo access token
     * @throws io.jsonwebtoken.JwtException si el token es inválido o expirado
     */
    AuthResponse execute(String refreshToken);
}