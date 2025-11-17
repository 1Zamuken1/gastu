package gastu.gastu.application.service.income;

import gastu.gastu.application.port.input.income.UpdateIngresoUseCase;
import gastu.gastu.application.port.output.IngresoRepositoryPort;
import gastu.gastu.domain.exception.BusinessException;
import gastu.gastu.domain.model.income.Ingreso;
import gastu.gastu.infrastructure.adapter.input.rest.dto.request.income.UpdateIngresoRequest;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.income.IngresoResponse;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.ConceptoIngresoEntity;
import gastu.gastu.infrastructure.adapter.output.persistence.repository.JpaConceptoIngresoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para actualizar ingresos
 */
@Service
@RequiredArgsConstructor
public class UpdateIngresoService implements UpdateIngresoUseCase {

    private final IngresoRepositoryPort ingresoRepository;
    private final JpaConceptoIngresoRepository conceptoRepository;

    @Override
    @Transactional
    public IngresoResponse execute(Long ingresoId, UpdateIngresoRequest request, Long usuarioId) {
        // 1. Buscar el ingreso
        Ingreso ingreso = ingresoRepository.findById(ingresoId)
                .orElseThrow(() -> new BusinessException("Ingreso no encontrado con ID: " + ingresoId));

        // 2. Verificar que el ingreso pertenece al usuario
        if (!ingreso.getUsuarioId().equals(usuarioId)) {
            throw new BusinessException("No tienes permiso para modificar este ingreso");
        }

        // 3. Validar que el concepto existe y está activo
        ConceptoIngresoEntity concepto = conceptoRepository.findById(request.getConceptoIngresoId())
                .orElseThrow(() -> new BusinessException(
                        "Concepto de ingreso no encontrado con ID: " + request.getConceptoIngresoId()));

        if (!concepto.isActivo()) {
            throw new BusinessException("El concepto de ingreso está inactivo");
        }

        // 4. Validar que el concepto sea válido para transacciones variables
        if (!concepto.getTipoTransaccion().esValidoParaVariable()) {
            throw new BusinessException(
                    "El concepto '" + concepto.getNombre() +
                            "' solo puede usarse para ingresos recurrentes (proyecciones)");
        }

        // 5. Actualizar campos
        ingreso.setMonto(request.getMonto());
        ingreso.setDescripcion(request.getDescripcion());
        ingreso.setFechaTransaccion(request.getFechaTransaccion());
        ingreso.setConceptoIngresoId(request.getConceptoIngresoId());

        if (request.getActivo() != null) {
            if (request.getActivo()) {
                ingreso.activar();
            } else {
                ingreso.desactivar();
            }
        }

        // 6. Validar
        if (!ingreso.isValido()) {
            throw new BusinessException("Los datos del ingreso son inválidos");
        }

        // 7. Guardar cambios
        Ingreso ingresoActualizado = ingresoRepository.save(ingreso);

        // 8. Retornar respuesta
        return mapToResponse(ingresoActualizado, concepto);
    }

    private IngresoResponse mapToResponse(Ingreso ingreso, ConceptoIngresoEntity concepto) {
        return IngresoResponse.builder()
                .ingresoId(ingreso.getIngresoId())
                .monto(ingreso.getMonto())
                .descripcion(ingreso.getDescripcion())
                .fechaTransaccion(ingreso.getFechaTransaccion())
                .fechaRegistro(ingreso.getFechaRegistro())
                .activo(ingreso.isActivo())
                .concepto(IngresoResponse.ConceptoIngresoSimpleResponse.builder()
                        .conceptoIngresoId(concepto.getConceptoIngresoId())
                        .nombre(concepto.getNombre())
                        .build())
                .build();
    }
}