package gastu.gastu.application.service.expense;

import gastu.gastu.domain.model.shared.TipoTransaccion;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.expense.ConceptoEgresoResponse;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.ConceptoEgresoEntity;
import gastu.gastu.infrastructure.adapter.output.persistence.repository.JpaConceptoEgresoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para obtener conceptos de egreso
 */
@Service
@RequiredArgsConstructor
public class GetConceptosEgresoService {

    private final JpaConceptoEgresoRepository repository;

    @Transactional(readOnly = true)
    public List<ConceptoEgresoResponse> getAllActivos() {
        return repository.findByActivoTrue().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ConceptoEgresoResponse> getByTipo(String tipo) {
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

    @Transactional(readOnly = true)
    public List<ConceptoEgresoResponse> getValidosParaRecurrente() {
        return repository.findValidosParaRecurrente().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ConceptoEgresoResponse> getValidosParaVariable() {
        return repository.findValidosParaVariable().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ConceptoEgresoResponse getById(Long id) {
        ConceptoEgresoEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Concepto de egreso no encontrado con ID: " + id));
        return toResponse(entity);
    }

    private ConceptoEgresoResponse toResponse(ConceptoEgresoEntity entity) {
        return ConceptoEgresoResponse.builder()
                .conceptoEgresoId(entity.getConceptoEgresoId())
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .tipoTransaccion(entity.getTipoTransaccion())
                .activo(entity.isActivo())
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }
}