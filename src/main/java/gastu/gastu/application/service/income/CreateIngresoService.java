package gastu.gastu.application.service.income;

import gastu.gastu.application.port.input.income.CreateIngresoUseCase;
import gastu.gastu.application.port.output.IngresoRepositoryPort;
import gastu.gastu.domain.exception.BusinessException;
import gastu.gastu.domain.model.income.Ingreso;
import gastu.gastu.infrastructure.adapter.input.rest.dto.request.income.CreateIngresoRequest;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.income.IngresoResponse;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.ConceptoIngresoEntity;
import gastu.gastu.infrastructure.adapter.output.persistence.repository.JpaConceptoIngresoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para crear ingresos
 */
@Service
@RequiredArgsConstructor
public class CreateIngresoService implements CreateIngresoUseCase {

    private final IngresoRepositoryPort ingresoRepository;
    private final JpaConceptoIngresoRepository conceptoRepository;

    @Override
    @Transactional
    public IngresoResponse execute(CreateIngresoRequest request, Long usuarioId) {
        // 1. Validar que el concepto existe y está activo
        ConceptoIngresoEntity concepto = conceptoRepository.findById(request.getConceptoIngresoId())
                .orElseThrow(() -> new BusinessException(
                        "Concepto de ingreso no encontrado con ID: " + request.getConceptoIngresoId()));

        if (!concepto.isActivo()) {
            throw new BusinessException("El concepto de ingreso está inactivo");
        }

        // 2. Validar que el concepto sea válido para transacciones variables
        if (!concepto.getTipoTransaccion().esValidoParaVariable()) {
            throw new BusinessException(
                    "El concepto '" + concepto.getNombre() +
                            "' solo puede usarse para ingresos recurrentes (proyecciones)");
        }

        // 3. Crear el ingreso
        Ingreso ingreso = Ingreso.builder()
                .monto(request.getMonto())
                .descripcion(request.getDescripcion())
                .fechaTransaccion(request.getFechaTransaccion())
                .conceptoIngresoId(request.getConceptoIngresoId())
                .usuarioId(usuarioId)
                .activo(true)
                .build();

        // 4. Validar el ingreso
        if (!ingreso.isValido()) {
            throw new BusinessException("Los datos del ingreso son inválidos");
        }

        // 5. Guardar
        Ingreso ingresoGuardado = ingresoRepository.save(ingreso);

        // 6. Retornar respuesta
        return mapToResponse(ingresoGuardado, concepto);
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