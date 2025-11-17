package gastu.gastu.infrastructure.adapter.input.rest.controller;

import gastu.gastu.application.service.income.CreateIngresoService;
import gastu.gastu.application.service.income.DeleteIngresoService;
import gastu.gastu.application.service.income.GetIngresosService;
import gastu.gastu.application.service.income.UpdateIngresoService;
import gastu.gastu.infrastructure.adapter.input.rest.dto.request.income.CreateIngresoRequest;
import gastu.gastu.infrastructure.adapter.input.rest.dto.request.income.UpdateIngresoRequest;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.income.IngresoResponse;
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
 * Controlador REST para Ingresos
 */
@RestController
@RequestMapping("/api/ingresos")
@RequiredArgsConstructor
@Tag(name = "Ingresos", description = "Gestión de ingresos del usuario")
@SecurityRequirement(name = "Bearer Authentication")
public class IngresoController {

    private final CreateIngresoService createService;
    private final GetIngresosService getService;
    private final UpdateIngresoService updateService;
    private final DeleteIngresoService deleteService;
    private final JwtAuthenticationHelper jwtAuthHelper;

    /**
     * Obtener todos los ingresos activos del usuario autenticado
     * GET /api/ingresos
     */
    @GetMapping
    @Operation(summary = "Listar ingresos activos", description = "Obtiene todos los ingresos activos del usuario autenticado")
    public ResponseEntity<List<IngresoResponse>> getAll(Authentication authentication) {
        Long usuarioId = getUserIdFromAuth(authentication);
        List<IngresoResponse> ingresos = getService.execute(usuarioId);
        return ResponseEntity.ok(ingresos);
    }

    /**
     * Obtener ingresos por rango de fechas
     * GET /api/ingresos/fecha?fechaInicio=2025-01-01&fechaFin=2025-01-31
     */
    @GetMapping("/fecha")
    @Operation(summary = "Listar ingresos por fecha", description = "Obtiene ingresos del usuario en un rango de fechas")
    public ResponseEntity<List<IngresoResponse>> getByFechaRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            Authentication authentication) {
        Long usuarioId = getUserIdFromAuth(authentication);
        List<IngresoResponse> ingresos = getService.getByFechaRange(usuarioId, fechaInicio, fechaFin);
        return ResponseEntity.ok(ingresos);
    }

    /**
     * Obtener todos los ingresos (incluidos inactivos)
     * GET /api/ingresos/todos
     */
    @GetMapping("/todos")
    @Operation(summary = "Listar todos los ingresos", description = "Obtiene todos los ingresos del usuario, incluidos los inactivos")
    public ResponseEntity<List<IngresoResponse>> getAllIncludeInactive(Authentication authentication) {
        Long usuarioId = getUserIdFromAuth(authentication);
        List<IngresoResponse> ingresos = getService.getAllByUsuario(usuarioId);
        return ResponseEntity.ok(ingresos);
    }

    /**
     * Obtener un ingreso por ID
     * GET /api/ingresos/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener ingreso por ID")
    public ResponseEntity<IngresoResponse> getById(
            @PathVariable Long id,
            Authentication authentication) {
        Long usuarioId = getUserIdFromAuth(authentication);
        IngresoResponse ingreso = getService.getById(id, usuarioId);
        return ResponseEntity.ok(ingreso);
    }

    /**
     * Crear un nuevo ingreso
     * POST /api/ingresos
     */
    @PostMapping
    @Operation(summary = "Crear ingreso", description = "Registra un nuevo ingreso para el usuario autenticado")
    public ResponseEntity<IngresoResponse> create(
            @Valid @RequestBody CreateIngresoRequest request,
            Authentication authentication) {
        Long usuarioId = getUserIdFromAuth(authentication);
        IngresoResponse response = createService.execute(request, usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Actualizar un ingreso existente
     * PUT /api/ingresos/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar ingreso", description = "Actualiza un ingreso existente")
    public ResponseEntity<IngresoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateIngresoRequest request,
            Authentication authentication) {
        Long usuarioId = getUserIdFromAuth(authentication);
        IngresoResponse response = updateService.execute(id, request, usuarioId);
        return ResponseEntity.ok(response);
    }

    /**
     * Desactivar un ingreso (soft delete)
     * DELETE /api/ingresos/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar ingreso", description = "Desactiva un ingreso (soft delete)")
    public ResponseEntity<Void> softDelete(
            @PathVariable Long id,
            Authentication authentication) {
        Long usuarioId = getUserIdFromAuth(authentication);
        deleteService.softDelete(id, usuarioId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Eliminar permanentemente un ingreso inactivo
     * DELETE /api/ingresos/{id}/permanente
     */
    @DeleteMapping("/{id}/permanente")
    @Operation(summary = "Eliminar ingreso permanentemente",
            description = "Elimina físicamente un ingreso inactivo de la base de datos")
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