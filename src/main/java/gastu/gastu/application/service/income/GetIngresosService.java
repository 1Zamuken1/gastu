package gastu.gastu.application.service.income;

import gastu.gastu.application.port.input.income.GetIngresosByUsuarioUseCase;
import gastu.gastu.application.port.output.IngresoRepositoryPort;
import gastu.gastu.domain.exception.BusinessException;
import gastu.gastu.domain.model.income.Ingreso;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.income.IngresoResponse;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.ConceptoIngresoEntity;
import gastu.gastu.infrastructure.adapter.output.persistence.repository.JpaConceptoIngresoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para obtener ingresos de un usuario
 */
@Service
@RequiredArgsConstructor
public class GetIngresosService implements GetIngresosByUsuarioUseCase {

    private final IngresoRepositoryPort ingresoRepository;
    private final JpaConceptoIngresoRepository conceptoRepository;

    /**
     * Obtiene todos los ingresos activos de un usuario
     */
    @Override
    @Transactional(readOnly = true)
    public List<IngresoResponse> execute(Long usuarioId) {
        List<Ingreso> ingresos = ingresoRepository.findByUsuarioIdAndActivoTrue(usuarioId);
        return mapToResponseList(ingresos);
    }

    /**
     * Obtiene ingresos de un usuario en un rango de fechas
     */
    @Transactional(readOnly = true)
    public List<IngresoResponse> getByFechaRange(Long usuarioId, LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio.isAfter(fechaFin)) {
            throw new BusinessException("La fecha de inicio no puede ser posterior a la fecha fin");
        }

        List<Ingreso> ingresos = ingresoRepository.findByUsuarioIdAndFechaTransaccionBetween(
                usuarioId, fechaInicio, fechaFin);
        return mapToResponseList(ingresos);
    }

    /**
     * Obtiene todos los ingresos de un usuario (incluidos inactivos)
     */
    @Transactional(readOnly = true)
    public List<IngresoResponse> getAllByUsuario(Long usuarioId) {
        List<Ingreso> ingresos = ingresoRepository.findByUsuarioId(usuarioId);
        return mapToResponseList(ingresos);
    }

    /**
     * Obtiene un ingreso por ID
     */
    @Transactional(readOnly = true)
    public IngresoResponse getById(Long ingresoId, Long usuarioId) {
        Ingreso ingreso = ingresoRepository.findById(ingresoId)
                .orElseThrow(() -> new BusinessException("Ingreso no encontrado con ID: " + ingresoId));

        // Verificar que el ingreso pertenece al usuario
        if (!ingreso.getUsuarioId().equals(usuarioId)) {
            throw new BusinessException("No tienes permiso para ver este ingreso");
        }

        ConceptoIngresoEntity concepto = conceptoRepository.findById(ingreso.getConceptoIngresoId())
                .orElse(null);

        return mapToResponse(ingreso, concepto);
    }

    private List<IngresoResponse> mapToResponseList(List<Ingreso> ingresos) {
        return ingresos.stream()
                .map(ingreso -> {
                    ConceptoIngresoEntity concepto = conceptoRepository.findById(ingreso.getConceptoIngresoId())
                            .orElse(null);
                    return mapToResponse(ingreso, concepto);
                })
                .collect(Collectors.toList());
    }

    private IngresoResponse mapToResponse(Ingreso ingreso, ConceptoIngresoEntity concepto) {
        IngresoResponse.ConceptoIngresoSimpleResponse conceptoResponse = null;
        if (concepto != null) {
            conceptoResponse = IngresoResponse.ConceptoIngresoSimpleResponse.builder()
                    .conceptoIngresoId(concepto.getConceptoIngresoId())
                    .nombre(concepto.getNombre())
                    .build();
        }

        return IngresoResponse.builder()
                .ingresoId(ingreso.getIngresoId())
                .monto(ingreso.getMonto())
                .descripcion(ingreso.getDescripcion())
                .fechaTransaccion(ingreso.getFechaTransaccion())
                .fechaRegistro(ingreso.getFechaRegistro())
                .activo(ingreso.isActivo())
                .concepto(conceptoResponse)
                .build();
    }
}