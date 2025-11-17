package gastu.gastu.application.service.expense;

import gastu.gastu.application.port.input.expense.UpdateEgresoUseCase;
import gastu.gastu.application.port.output.EgresoRepositoryPort;
import gastu.gastu.domain.exception.BusinessException;
import gastu.gastu.domain.model.expense.Egreso;
import gastu.gastu.infrastructure.adapter.input.rest.dto.request.expense.UpdateEgresoRequest;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.expense.EgresoResponse;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.ConceptoEgresoEntity;
import gastu.gastu.infrastructure.adapter.output.persistence.repository.JpaConceptoEgresoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para actualizar egresos
 */
@Service
@RequiredArgsConstructor
public class UpdateEgresoService implements UpdateEgresoUseCase {

    private final EgresoRepositoryPort egresoRepository;
    private final JpaConceptoEgresoRepository conceptoRepository;

    @Override
    @Transactional
    public EgresoResponse execute(Long egresoId, UpdateEgresoRequest request, Long usuarioId) {
        // 1. Buscar el egreso
        Egreso egreso = egresoRepository.findById(egresoId)
                .orElseThrow(() -> new BusinessException("Egreso no encontrado con ID: " + egresoId));

        // 2. Verificar que el egreso pertenece al usuario
        if (!egreso.getUsuarioId().equals(usuarioId)) {
            throw new BusinessException("No tienes permiso para modificar este egreso");
        }

        // 3. Validar que el concepto existe y está activo
        ConceptoEgresoEntity concepto = conceptoRepository.findById(request.getConceptoEgresoId())
                .orElseThrow(() -> new BusinessException(
                        "Concepto de egreso no encontrado con ID: " + request.getConceptoEgresoId()));

        if (!concepto.isActivo()) {
            throw new BusinessException("El concepto de egreso está inactivo");
        }

        // 4. Validar que el concepto sea válido para transacciones variables
        if (!concepto.getTipoTransaccion().esValidoParaVariable()) {
            throw new BusinessException(
                    "El concepto '" + concepto.getNombre() +
                            "' solo puede usarse para egresos recurrentes (proyecciones)");
        }

        // 5. Actualizar campos
        egreso.setMonto(request.getMonto());
        egreso.setDescripcion(request.getDescripcion());
        egreso.setFechaTransaccion(request.getFechaTransaccion());
        egreso.setConceptoEgresoId(request.getConceptoEgresoId());

        if (request.getActivo() != null) {
            if (request.getActivo()) {
                egreso.activar();
            } else {
                egreso.desactivar();
            }
        }

        // 6. Validar
        if (!egreso.isValido()) {
            throw new BusinessException("Los datos del egreso son inválidos");
        }

        // 7. Guardar cambios
        Egreso egresoActualizado = egresoRepository.save(egreso);

        // 8. Retornar respuesta
        return mapToResponse(egresoActualizado, concepto);
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