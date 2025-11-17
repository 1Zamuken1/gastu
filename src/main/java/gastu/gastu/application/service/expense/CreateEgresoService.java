package gastu.gastu.application.service.expense;

import gastu.gastu.application.port.input.expense.CreateEgresoUseCase;
import gastu.gastu.application.port.output.EgresoRepositoryPort;
import gastu.gastu.domain.exception.BusinessException;
import gastu.gastu.domain.model.expense.Egreso;
import gastu.gastu.infrastructure.adapter.input.rest.dto.request.expense.CreateEgresoRequest;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.expense.EgresoResponse;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.ConceptoEgresoEntity;
import gastu.gastu.infrastructure.adapter.output.persistence.repository.JpaConceptoEgresoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para crear egresos
 */
@Service
@RequiredArgsConstructor
public class CreateEgresoService implements CreateEgresoUseCase {

    private final EgresoRepositoryPort egresoRepository;
    private final JpaConceptoEgresoRepository conceptoRepository;

    @Override
    @Transactional
    public EgresoResponse execute(CreateEgresoRequest request, Long usuarioId) {
        // 1. Validar que el concepto existe y está activo
        ConceptoEgresoEntity concepto = conceptoRepository.findById(request.getConceptoEgresoId())
                .orElseThrow(() -> new BusinessException(
                        "Concepto de egreso no encontrado con ID: " + request.getConceptoEgresoId()));

        if (!concepto.isActivo()) {
            throw new BusinessException("El concepto de egreso está inactivo");
        }

        // 2. Validar que el concepto sea válido para transacciones variables
        if (!concepto.getTipoTransaccion().esValidoParaVariable()) {
            throw new BusinessException(
                    "El concepto '" + concepto.getNombre() +
                            "' solo puede usarse para egresos recurrentes (proyecciones)");
        }

        // 3. Crear el egreso
        Egreso egreso = Egreso.builder()
                .monto(request.getMonto())
                .descripcion(request.getDescripcion())
                .fechaTransaccion(request.getFechaTransaccion())
                .conceptoEgresoId(request.getConceptoEgresoId())
                .usuarioId(usuarioId)
                .activo(true)
                .build();

        // 4. Validar el egreso
        if (!egreso.isValido()) {
            throw new BusinessException("Los datos del egreso son inválidos");
        }

        // 5. Guardar
        Egreso egresoGuardado = egresoRepository.save(egreso);

        // 6. Retornar respuesta
        return mapToResponse(egresoGuardado, concepto);
    }

    private EgresoResponse mapToResponse(Egreso egreso, ConceptoEgresoEntity concepto) {
        return EgresoResponse.builder()
                .egresoId(egreso.getEgresoId())
                .monto(egreso.getMonto())
                .descripcion(egreso.getDescripcion())
                .fechaTransaccion(egreso.getFechaTransaccion())
                .fechaRegistro(egreso.getFechaRegistro())
                .activo(egreso.isActivo())
                .concepto(EgresoResponse.ConceptoEgresoSimpleResponse.builder()
                        .conceptoEgresoId(concepto.getConceptoEgresoId())
                        .nombre(concepto.getNombre())
                        .build())
                .build();
    }
}