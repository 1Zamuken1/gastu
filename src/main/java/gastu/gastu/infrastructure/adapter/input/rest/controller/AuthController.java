package gastu.gastu.infrastructure.adapter.input.rest.controller;

import gastu.gastu.application.port.input.auth.LoginUseCase;
import gastu.gastu.application.port.input.auth.RefreshTokenUseCase;
import gastu.gastu.application.port.input.auth.RegisterUserUseCase;
import gastu.gastu.infrastructure.adapter.input.rest.dto.request.auth.LoginRequest;
import gastu.gastu.infrastructure.adapter.input.rest.dto.request.auth.RegisterRequest;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.auth.AuthResponse;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.auth.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para endpoints de autenticación
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Endpoints para login, registro y refresh token")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final RegisterUserUseCase registerUserUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final PasswordEncoder passwordEncoder; // prueba contraseña

    /**
     * ENDPOINT TEMPORAL - Solo para desarrollo
     * Genera hash BCrypt de una contraseña
     */
    @GetMapping("/hash-password")
    public ResponseEntity<Map<String, String>> hashPassword(@RequestParam String password) {
        String hash = passwordEncoder.encode(password);
        Map<String, String> response = new HashMap<>();
        response.put("plainPassword", password);
        response.put("bcryptHash", hash);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para login de usuarios
     * POST /api/auth/login
     */
    @PostMapping("/login")
    @Operation(summary = "Login de usuario", description = "Autentica un usuario y retorna tokens JWT")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = loginUseCase.execute(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para registro de nuevos usuarios
     * POST /api/auth/register
     */
    @PostMapping("/register")
    @Operation(summary = "Registro de usuario", description = "Registra un nuevo usuario en el sistema")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse response = registerUserUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Endpoint para refrescar el access token
     * POST /api/auth/refresh
     */
    @PostMapping("/refresh")
    @Operation(summary = "Refrescar token", description = "Genera un nuevo access token usando el refresh token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestHeader("Authorization") String authHeader) {
        // Extraer el token del header "Bearer <token>"
        String refreshToken = authHeader.substring(7);
        AuthResponse response = refreshTokenUseCase.execute(refreshToken);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint de prueba para verificar que el servidor está funcionando
     * GET /api/auth/health
     */
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Verifica que la API esté funcionando")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("GASTU API está funcionando correctamente ✅");
    }
}