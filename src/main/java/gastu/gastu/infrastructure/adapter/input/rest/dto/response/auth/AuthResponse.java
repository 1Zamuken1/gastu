package gastu.gastu.infrastructure.adapter.input.rest.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para autenticación exitosa
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private UserResponse usuario;

    // Constructor adicional con tokenType por defecto
    public AuthResponse(String accessToken, String refreshToken, UserResponse usuario) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = "Bearer";
        this.usuario = usuario;
    }
}