package gastu.gastu.application.service.income;

import gastu.gastu.domain.exception.BusinessException;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.ConceptoIngresoEntity;
import gastu.gastu.infrastructure.adapter.output.persistence.repository.JpaConceptoIngresoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteConceptoIngresoService {

    private final JpaConceptoIngresoRepository repository;

    @Transactional  // ⬅️ CRÍTICO: Debe tener esta anotación
    public void delete(Long id) {
        ConceptoIngresoEntity entity = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Concepto de ingreso no encontrado con ID: " + id));

        // Desactivar en lugar de eliminar físicamente
        entity.setActivo(false);
        repository.save(entity);  // ⬅️ Debe ejecutar save()
    }
}