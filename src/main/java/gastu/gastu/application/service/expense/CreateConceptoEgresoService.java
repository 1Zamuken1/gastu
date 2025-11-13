package gastu.gastu.application.service.expense;

import gastu.gastu.domain.exception.BusinessException;
import gastu.gastu.infrastructure.adapter.input.rest.dto.request.expense.CreateConceptoEgresoRequest;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.expense.ConceptoEgresoResponse;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.ConceptoIngresoEntity;
import gastu.gastu.infrastructure.adapter.output.persistence.repository.JpaConceptoIngresoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para crear conceptos de egreso
 */
@Service
@RequiredArgsConstructor
public class CreateConceptoEgresoService {

    private final JpaConceptoIngresoRepository repository;

    /**
     * Crea un nuevo concepto de egreso
     */
    @Transactional
    public ConceptoEgresoResponse create(CreateConceptoEgresoRequest request) {
        // Validar que no exista un concepto con el mismo nombre
        if (repository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new BusinessException("Ya existe un concepto de egreso con el nombre: " + request.getNombre());
        }

        // Crear la entidad
        ConceptoIngresoEntity entity = ConceptoIngresoEntity.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .tipoTransaccion(request.getTipoTransaccion())
                .activo(true)
                .build();

        // Guardar
        ConceptoIngresoEntity saved = repository.save(entity);

        // Retornar respuesta
        return ConceptoEgresoResponse.builder()
                .conceptoEgresoId(saved.getConceptoIngresoId())
                .nombre(saved.getNombre())
                .descripcion(saved.getDescripcion())
                .tipoTransaccion(saved.getTipoTransaccion())
                .activo(saved.isActivo())
                .fechaCreacion(saved.getFechaCreacion())
                .build();
    }
}