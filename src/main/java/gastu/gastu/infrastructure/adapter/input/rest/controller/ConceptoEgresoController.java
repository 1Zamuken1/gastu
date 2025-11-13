package gastu.gastu.infrastructure.adapter.input.rest.controller;

import gastu.gastu.application.service.expense.CreateConceptoEgresoService;
import gastu.gastu.application.service.expense.GetConceptosEgresoService;
import gastu.gastu.infrastructure.adapter.input.rest.dto.request.expense.CreateConceptoEgresoRequest;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.expense.ConceptoEgresoResponse;
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
 * Controlador REST para Conceptos de Egreso
 */
@RestController
@RequestMapping("/api/conceptos-egreso")
@RequiredArgsConstructor
@Tag(name = "Conceptos de Egreso", description = "Gestión de conceptos/categorías de egresos")
@SecurityRequirement(name = "Bearer Authentication")
public class ConceptoEgresoController {

    private final GetConceptosEgresoService getService;
    private final CreateConceptoEgresoService createService;

    @GetMapping
    @Operation(summary = "Listar conceptos de egreso")
    public ResponseEntity<List<ConceptoEgresoResponse>> getAll(
            @RequestParam(required = false) String tipo) {

        List<ConceptoEgresoResponse> conceptos;

        if (tipo != null && !tipo.trim().isEmpty()) {
            conceptos = getService.getByTipo(tipo);
        } else {
            conceptos = getService.getAllActivos();
        }

        return ResponseEntity.ok(conceptos);
    }

    @GetMapping("/recurrentes")
    @Operation(summary = "Conceptos para egresos recurrentes")
    public ResponseEntity<List<ConceptoEgresoResponse>> getRecurrentes() {
        return ResponseEntity.ok(getService.getValidosParaRecurrente());
    }

    @GetMapping("/variables")
    @Operation(summary = "Conceptos para egresos variables")
    public ResponseEntity<List<ConceptoEgresoResponse>> getVariables() {
        return ResponseEntity.ok(getService.getValidosParaVariable());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener concepto por ID")
    public ResponseEntity<ConceptoEgresoResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(getService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Crear concepto de egreso", description = "Solo administradores")
    public ResponseEntity<ConceptoEgresoResponse> create(
            @Valid @RequestBody CreateConceptoEgresoRequest request) {
        ConceptoEgresoResponse response = createService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}