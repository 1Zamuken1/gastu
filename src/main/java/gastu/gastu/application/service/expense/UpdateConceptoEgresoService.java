package gastu.gastu.application.service.expense;

import gastu.gastu.domain.exception.BusinessException;
import gastu.gastu.infrastructure.adapter.input.rest.dto.request.expense.CreateConceptoEgresoRequest;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.expense.ConceptoEgresoResponse;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.ConceptoEgresoEntity;
import gastu.gastu.infrastructure.adapter.output.persistence.repository.JpaConceptoEgresoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para actualizar conceptos de egreso
 */
@Service
@RequiredArgsConstructor
public class UpdateConceptoEgresoService {

    private final JpaConceptoEgresoRepository repository;

    /**
     * Actualiza un concepto de egreso existente
     */
    @Transactional
    public ConceptoEgresoResponse update(Long id, CreateConceptoEgresoRequest request) {
        // Buscar el concepto
        ConceptoEgresoEntity entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Concepto de egreso no encontrado con ID: " + id));

        // Validar que el nuevo nombre no esté en uso (si cambió)
        if (!entity.getNombre().equalsIgnoreCase(request.getNombre())
                && repository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new BusinessException("Ya existe un concepto de egreso con el nombre: " + request.getNombre());
        }

        // Actualizar datos
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setTipoTransaccion(request.getTipoTransaccion());

        // Guardar cambios
        ConceptoEgresoEntity updated = repository.save(entity);

        // Retornar respuesta
        return ConceptoEgresoResponse.builder()
                .conceptoEgresoId(updated.getConceptoEgresoId())
                .nombre(updated.getNombre())
                .descripcion(updated.getDescripcion())
                .tipoTransaccion(updated.getTipoTransaccion())
                .activo(updated.isActivo())
                .fechaCreacion(updated.getFechaCreacion())
                .build();
    }
}
