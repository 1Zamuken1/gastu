package gastu.gastu.infrastructure.adapter.input.rest.controller;

import gastu.gastu.application.service.expense.CreateEgresoService;
import gastu.gastu.application.service.expense.DeleteEgresoService;
import gastu.gastu.application.service.expense.GetEgresosService;
import gastu.gastu.application.service.expense.UpdateEgresoService;
import gastu.gastu.infrastructure.adapter.input.rest.dto.request.expense.CreateEgresoRequest;
import gastu.gastu.infrastructure.adapter.input.rest.dto.request.expense.UpdateEgresoRequest;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.expense.EgresoResponse;
import gastu.gastu.infrastructure.config.security.JwtAuthenticationHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para Egresos
 */
@RestController
@RequestMapping("/api/egresos")
@RequiredArgsConstructor
@Tag(name = "Egresos", description = "Gestión de egresos del usuario")
@SecurityRequirement(name = "Bearer Authentication")
public class EgresoController {

    private final CreateEgresoService createService;
    private final GetEgresosService getService;
    private final UpdateEgresoService updateService;
    private final DeleteEgresoService deleteService;
    private final JwtAuthenticationHelper jwtAuthHelper;

    /**
     * Obtener todos los egresos activos del usuario autenticado
     * GET /api/egresos
     */
    @GetMapping
    @Operation(summary = "Listar egresos activos", description = "Obtiene todos los egresos activos del usuario autenticado")
    public ResponseEntity<List<EgresoResponse>> getAll(Authentication authentication) {
        Long usuarioId = getUserIdFromAuth(authentication);
        List<EgresoResponse> egresos = getService.execute(usuarioId);
        return ResponseEntity.ok(egresos);
    }

    /**
     * Obtener egresos por rango de fechas
     * GET /api/egresos/fecha?fechaInicio=2025-01-01&fechaFin=2025-01-31
     */
    @GetMapping("/fecha")
    @Operation(summary = "Listar egresos por fecha", description = "Obtiene egresos del usuario en un rango de fechas")
    public ResponseEntity<List<EgresoResponse>> getByFechaRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            Authentication authentication) {
        Long usuarioId = getUserIdFromAuth(authentication);
        List<EgresoResponse> egresos = getService.getByFechaRange(usuarioId, fechaInicio, fechaFin);
        return ResponseEntity.ok(egresos);
    }

    /**
     * Obtener todos los egresos (incluidos inactivos)
     * GET /api/egresos/todos
     */
    @GetMapping("/todos")
    @Operation(summary = "Listar todos los egresos", description = "Obtiene todos los egresos del usuario, incluidos los inactivos")
    public ResponseEntity<List<EgresoResponse>> getAllIncludeInactive(Authentication authentication) {
        Long usuarioId = getUserIdFromAuth(authentication);
        List<EgresoResponse> egresos = getService.getAllByUsuario(usuarioId);
        return ResponseEntity.ok(egresos);
    }

    /**
     * Obtener un egreso por ID
     * GET /api/egresos/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener egreso por ID")
    public ResponseEntity<EgresoResponse> getById(
            @PathVariable Long id,
            Authentication authentication) {
        Long usuarioId = getUserIdFromAuth(authentication);
        EgresoResponse egreso = getService.getById(id, usuarioId);
        return ResponseEntity.ok(egreso);
    }

    /**
     * Crear un nuevo egreso
     * POST /api/egresos
     */
    @PostMapping
    @Operation(summary = "Crear egreso", description = "Registra un nuevo egreso para el usuario autenticado")
    public ResponseEntity<EgresoResponse> create(
            @Valid @RequestBody CreateEgresoRequest request,
            Authentication authentication) {
        Long usuarioId = getUserIdFromAuth(authentication);
        EgresoResponse response = createService.execute(request, usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Actualizar un egreso existente
     * PUT /api/egresos/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar egreso", description = "Actualiza un egreso existente")
    public ResponseEntity<EgresoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEgresoRequest request,
            Authentication authentication) {
        Long usuarioId = getUserIdFromAuth(authentication);
        EgresoResponse response = updateService.execute(id, request, usuarioId);
        return ResponseEntity.ok(response);
    }

    /**
     * Desactivar un egreso (soft delete)
     * DELETE /api/egresos/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar egreso", description = "Desactiva un egreso (soft delete)")
    public ResponseEntity<Void> softDelete(
            @PathVariable Long id,
            Authentication authentication) {
        Long usuarioId = getUserIdFromAuth(authentication);
        deleteService.softDelete(id, usuarioId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Eliminar permanentemente un egreso inactivo
     * DELETE /api/egresos/{id}/permanente
     */
    @DeleteMapping("/{id}/permanente")
    @Operation(summary = "Eliminar egreso permanentemente",
            description = "Elimina físicamente un egreso inactivo de la base de datos")
    public ResponseEntity<Void> hardDelete(
            @PathVariable Long id,
            Authentication authentication) {
        Long usuarioId = getUserIdFromAuth(authentication);
        deleteService.hardDelete(id, usuarioId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Extrae el usuarioId del token JWT
     */
    private Long getUserIdFromAuth(Authentication authentication) {
        return jwtAuthHelper.getUserIdFromAuthentication(authentication);
    }
}