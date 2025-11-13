package gastu.gastu.application.service.income;

import gastu.gastu.domain.model.shared.TipoTransaccion;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.income.ConceptoIngresoResponse;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.ConceptoIngresoEntity;
import gastu.gastu.infrastructure.adapter.output.persistence.repository.JpaConceptoIngresoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para obtener conceptos de ingreso
 */
@Service
@RequiredArgsConstructor
public class GetConceptosIngresoService {

    private final JpaConceptoIngresoRepository repository;

    /**
     * Obtiene todos los conceptos activos
     */
    @Transactional(readOnly = true)
    public List<ConceptoIngresoResponse> getAllActivos() {
        return repository.findByActivoTrue().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene conceptos filtrados por tipo de transacción
     */
    @Transactional(readOnly = true)
    public List<ConceptoIngresoResponse> getByTipo(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            return getAllActivos();
        }

        TipoTransaccion tipoTransaccion;
        try {
            tipoTransaccion = TipoTransaccion.valueOf(tipo.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de transacción inválido: " + tipo);
        }

        return repository.findByTipoTransaccionAndActivoTrue(tipoTransaccion).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene conceptos válidos para ingresos recurrentes
     */
    @Transactional(readOnly = true)
    public List<ConceptoIngresoResponse> getValidosParaRecurrente() {
        return repository.findValidosParaRecurrente().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene conceptos válidos para ingresos variables
     */
    @Transactional(readOnly = true)
    public List<ConceptoIngresoResponse> getValidosParaVariable() {
        return repository.findValidosParaVariable().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un concepto por ID
     */
    @Transactional(readOnly = true)
    public ConceptoIngresoResponse getById(Long id) {
        ConceptoIngresoEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Concepto de ingreso no encontrado con ID: " + id));
        return toResponse(entity);
    }

    /**
     * Convierte Entity a Response DTO
     */
    private ConceptoIngresoResponse toResponse(ConceptoIngresoEntity entity) {
        return ConceptoIngresoResponse.builder()
                .conceptoIngresoId(entity.getConceptoIngresoId())
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .tipoTransaccion(entity.getTipoTransaccion())
                .activo(entity.isActivo())
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }
}