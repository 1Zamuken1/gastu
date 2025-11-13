package gastu.gastu.application.service.expense;

import gastu.gastu.domain.exception.BusinessException;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.ConceptoEgresoEntity;
import gastu.gastu.infrastructure.adapter.output.persistence.repository.JpaConceptoEgresoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para eliminar (desactivar) conceptos de egreso
 */
@Service
@RequiredArgsConstructor
public class DeleteConceptoEgresoService {

    private final JpaConceptoEgresoRepository repository;

    /**
     * Desactiva un concepto de egreso (soft delete)
     */
    @Transactional
    public void delete(Long id) {
        ConceptoEgresoEntity entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Concepto de egreso no encontrado con ID: " + id));

        // Desactivar en lugar de eliminar físicamente
        entity.setActivo(false);
        repository.save(entity);
    }
}
