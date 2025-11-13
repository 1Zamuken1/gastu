package gastu.gastu.application.service.income;

import gastu.gastu.domain.exception.BusinessException;
import gastu.gastu.infrastructure.adapter.input.rest.dto.request.income.CreateConceptoIngresoRequest;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.income.ConceptoIngresoResponse;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.ConceptoIngresoEntity;
import gastu.gastu.infrastructure.adapter.output.persistence.repository.JpaConceptoIngresoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para actualizar conceptos de ingreso
 */
@Service
@RequiredArgsConstructor
public class UpdateConceptoIngresoService {

    private final JpaConceptoIngresoRepository repository;

    /**
     * Actualiza un concepto de ingreso existente
     */
    @Transactional
    public ConceptoIngresoResponse update(Long id, CreateConceptoIngresoRequest request) {
        // Buscar el concepto
        ConceptoIngresoEntity entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Concepto de ingreso no encontrado con ID: " + id));

        // Validar que el nuevo nombre no esté en uso (si cambió)
        if (!entity.getNombre().equalsIgnoreCase(request.getNombre())
                && repository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new BusinessException("Ya existe un concepto de ingreso con el nombre: " + request.getNombre());
        }

        // Actualizar datos
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setTipoTransaccion(request.getTipoTransaccion());

        // Guardar cambios
        ConceptoIngresoEntity updated = repository.save(entity);

        // Retornar respuesta
        return ConceptoIngresoResponse.builder()
                .conceptoIngresoId(updated.getConceptoIngresoId())
                .nombre(updated.getNombre())
                .descripcion(updated.getDescripcion())
                .tipoTransaccion(updated.getTipoTransaccion())
                .activo(updated.isActivo())
                .fechaCreacion(updated.getFechaCreacion())
                .build();
    }
}