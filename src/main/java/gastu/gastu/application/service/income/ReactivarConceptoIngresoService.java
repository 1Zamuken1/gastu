package gastu.gastu.application.service.income;

import gastu.gastu.domain.exception.BusinessException;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.income.ConceptoIngresoResponse;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.ConceptoIngresoEntity;
import gastu.gastu.infrastructure.adapter.output.persistence.repository.JpaConceptoIngresoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para reactivar conceptos de ingreso desactivados
 */
@Service
@RequiredArgsConstructor
public class ReactivarConceptoIngresoService {

    private final JpaConceptoIngresoRepository repository;

    /**
     * Reactiva un concepto de ingreso previamente desactivado
     */
    @Transactional
    public ConceptoIngresoResponse reactivar(Long id) {
        ConceptoIngresoEntity entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Concepto de ingreso no encontrado con ID: " + id));

        if (entity.isActivo()) {
            throw new BusinessException("El concepto ya está activo");
        }

        // Reactivar
        entity.setActivo(true);
        ConceptoIngresoEntity saved = repository.save(entity);

        return ConceptoIngresoResponse.builder()
                .conceptoIngresoId(saved.getConceptoIngresoId())
                .nombre(saved.getNombre())
                .descripcion(saved.getDescripcion())
                .tipoTransaccion(saved.getTipoTransaccion())
                .activo(saved.isActivo())
                .fechaCreacion(saved.getFechaCreacion())
                .build();
    }
}