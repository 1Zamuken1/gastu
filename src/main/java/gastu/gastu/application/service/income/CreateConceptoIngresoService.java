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
 * Servicio para crear conceptos de ingreso
 */
@Service
@RequiredArgsConstructor
public class CreateConceptoIngresoService {

    private final JpaConceptoIngresoRepository repository;

    /**
     * Crea un nuevo concepto de ingreso
     */
    @Transactional
    public ConceptoIngresoResponse create(CreateConceptoIngresoRequest request) {
        // Validar que no exista un concepto con el mismo nombre
        if (repository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new BusinessException("Ya existe un concepto de ingreso con el nombre: " + request.getNombre());
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