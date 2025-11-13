package gastu.gastu.infrastructure.adapter.input.rest.controller;

import gastu.gastu.application.service.income.*;
import gastu.gastu.infrastructure.adapter.input.rest.dto.request.income.CreateConceptoIngresoRequest;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.income.ConceptoIngresoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para Conceptos de Ingreso
 */
@RestController
@RequestMapping("/api/conceptos-ingreso")
@RequiredArgsConstructor
@Tag(name = "Conceptos de Ingreso", description = "Gestión de conceptos/categorías de ingresos")
@SecurityRequirement(name = "Bearer Authentication")
public class ConceptoIngresoController {

    private final GetConceptosIngresoService getService;
    private final CreateConceptoIngresoService createService;
    private final UpdateConceptoIngresoService updateService;
    private final DeleteConceptoIngresoService deleteService;

    /**
     * Listar todos los conceptos activos (opcionalmente filtrados por tipo)
     * GET /api/conceptos-ingreso
     * GET /api/conceptos-ingreso?tipo=RECURRENTE
     */
    @GetMapping
    @Operation(summary = "Listar conceptos de ingreso", description = "Obtiene todos los conceptos activos, puede filtrar por tipo")
    public ResponseEntity<List<ConceptoIngresoResponse>> getAll(
            @RequestParam(required = false) String tipo) {

        List<ConceptoIngresoResponse> conceptos;

        if (tipo != null && !tipo.trim().isEmpty()) {
            conceptos = getService.getByTipo(tipo);
        } else {
            conceptos = getService.getAllActivos();
        }

        return ResponseEntity.ok(conceptos);
    }

    /**
     * Obtener conceptos válidos para ingresos recurrentes
     * GET /api/conceptos-ingreso/recurrentes
     */
    @GetMapping("/recurrentes")
    @Operation(summary = "Conceptos para ingresos recurrentes", description = "Obtiene conceptos válidos para registrar ingresos recurrentes (RECURRENTE o AMBOS)")
    public ResponseEntity<List<ConceptoIngresoResponse>> getRecurrentes() {
        return ResponseEntity.ok(getService.getValidosParaRecurrente());
    }

    /**
     * Obtener conceptos válidos para ingresos variables
     * GET /api/conceptos-ingreso/variables
     */
    @GetMapping("/variables")
    @Operation(summary = "Conceptos para ingresos variables", description = "Obtiene conceptos válidos para registrar ingresos variables (VARIABLE o AMBOS)")
    public ResponseEntity<List<ConceptoIngresoResponse>> getVariables() {
        return ResponseEntity.ok(getService.getValidosParaVariable());
    }

    /**
     * Obtener un concepto por ID
     * GET /api/conceptos-ingreso/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener concepto por ID")
    public ResponseEntity<ConceptoIngresoResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(getService.getById(id));
    }

    /**
     * Crear un nuevo concepto (solo ADMIN)
     * POST /api/conceptos-ingreso
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Crear concepto de ingreso", description = "Solo administradores pueden crear conceptos")
    public ResponseEntity<ConceptoIngresoResponse> create(
            @Valid @RequestBody CreateConceptoIngresoRequest request) {
        ConceptoIngresoResponse response = createService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Actualizar un concepto existente (solo ADMIN)
     * PUT /api/conceptos-ingreso/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Actualizar concepto de ingreso", description = "Solo administradores pueden actualizar conceptos")
    public ResponseEntity<ConceptoIngresoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateConceptoIngresoRequest request) {
        ConceptoIngresoResponse response = updateService.update(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Eliminar (desactivar) un concepto (solo ADMIN)
     * DELETE /api/conceptos-ingreso/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Eliminar concepto de ingreso", description = "Solo administradores pueden eliminar conceptos (soft delete)")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}