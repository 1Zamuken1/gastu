package gastu.gastu.infrastructure.adapter.input.rest.controller;

import gastu.gastu.application.port.input.income.*;
import gastu.gastu.infrastructure.adapter.input.rest.dto.request.income.CreateIngresoRequest;
import gastu.gastu.infrastructure.adapter.input.rest.dto.request.income.UpdateIngresoRequest;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.income.IngresoResponse;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.UsuarioEntity;
import gastu.gastu.infrastructure.adapter.output.persistence.repository.JpaUsuarioRepository;
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
@Tag(name = "Ingresos", description = "Gestión de ingresos variables del usuario")
@SecurityRequirement(name = "Bearer Authentication")
public class IngresoController {

    private final CreateIngresoUseCase createIngresoUseCase;
    private final GetIngresosByUsuarioUseCase getIngresosUseCase;
    private final UpdateIngresoUseCase updateIngresoUseCase;
    private final DeleteIngresoUseCase deleteIngresoUseCase;
    private final JpaUsuarioRepository usuarioRepository;

    /**
     * Crear un nuevo ingreso
     * POST /api/ingresos
     */
    @PostMapping
    @Operation(summary = "Crear ingreso", description = "Registra un nuevo ingreso variable")
    public ResponseEntity<IngresoResponse> create(
            @Valid @RequestBody CreateIngresoRequest request,
            Authentication authentication) {

        Long usuarioId = getUsuarioIdFromAuth(authentication);
        IngresoResponse response = createIngresoUseCase.execute(request, usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Listar todos los ingresos activos del usuario
     * GET /api/ingresos
     */
    @GetMapping
    @Operation(summary = "Listar ingresos activos", description = "Obtiene todos los ingresos activos del usuario")
    public ResponseEntity<List<IngresoResponse>> getAllActivos(Authentication authentication) {
        Long usuarioId = getUsuarioIdFromAuth(authentication);
        List<IngresoResponse> ingresos = getIngresosUseCase.getAllActivos(usuarioId);
        return ResponseEntity.ok(ingresos);
    }

    /**
     * Listar todos los ingresos (activos e inactivos)
     * GET /api/ingresos/all
     */
    @GetMapping("/all")
    @Operation(summary = "Listar todos los ingresos", description = "Obtiene todos los ingresos del usuario (activos e inactivos)")
    public ResponseEntity<List<IngresoResponse>> getAll(Authentication authentication) {
        Long usuarioId = getUsuarioIdFromAuth(authentication);
        List<IngresoResponse> ingresos = getIngresosUseCase.getAll(usuarioId);
        return ResponseEntity.ok(ingresos);
    }

    /**
     * Obtener ingresos por rango de fechas
     * GET /api/ingresos/rango?fechaInicio=2025-01-01&fechaFin=2025-01-31
     */
    @GetMapping("/rango")
    @Operation(summary = "Ingresos por rango de fechas", description = "Obtiene ingresos entre dos fechas")
    public ResponseEntity<List<IngresoResponse>> getByFechaRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            Authentication authentication) {

        Long usuarioId = getUsuarioIdFromAuth(authentication);
        List<IngresoResponse> ingresos = getIngresosUseCase.getByFechaRange(usuarioId, fechaInicio, fechaFin);
        return ResponseEntity.ok(ingresos);
    }

    /**
     * Obtener ingresos de un mes específico
     * GET /api/ingresos/mes?mes=11&anio=2025
     */
    @GetMapping("/mes")
    @Operation(summary = "Ingresos por mes", description = "Obtiene ingresos de un mes específico")
    public ResponseEntity<List<IngresoResponse>> getByMes(
            @RequestParam int mes,
            @RequestParam int anio,
            Authentication authentication) {

        Long usuarioId = getUsuarioIdFromAuth(authentication);
        List<IngresoResponse> ingresos = getIngresosUseCase.getByMes(usuarioId, mes, anio);
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

        Long usuarioId = getUsuarioIdFromAuth(authentication);
        IngresoResponse ingreso = getIngresosUseCase.getById(id, usuarioId);
        return ResponseEntity.ok(ingreso);
    }

    /**
     * Actualizar un ingreso
     * PUT /api/ingresos/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar ingreso")
    public ResponseEntity<IngresoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateIngresoRequest request,
            Authentication authentication) {

        Long usuarioId = getUsuarioIdFromAuth(authentication);
        IngresoResponse response = updateIngresoUseCase.execute(id, request, usuarioId);
        return ResponseEntity.ok(response);
    }

    /**
     * Desactivar un ingreso (soft delete)
     * DELETE /api/ingresos/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar ingreso", description = "Desactiva un ingreso (soft delete)")
    public ResponseEntity<Void> desactivar(
            @PathVariable Long id,
            Authentication authentication) {

        Long usuarioId = getUsuarioIdFromAuth(authentication);
        deleteIngresoUseCase.desactivar(id, usuarioId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Eliminar permanentemente un ingreso (hard delete)
     * DELETE /api/ingresos/{id}/permanente
     */
    @DeleteMapping("/{id}/permanente")
    @Operation(summary = "Eliminar ingreso permanentemente", description = "Elimina un ingreso permanentemente (debe estar desactivado)")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id,
            Authentication authentication) {

        Long usuarioId = getUsuarioIdFromAuth(authentication);
        deleteIngresoUseCase.eliminar(id, usuarioId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Extrae el ID del usuario desde el Authentication
     */
    private Long getUsuarioIdFromAuth(Authentication authentication) {
        String correo = authentication.getName();
        UsuarioEntity usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuario.getUsuarioId();
    }
}