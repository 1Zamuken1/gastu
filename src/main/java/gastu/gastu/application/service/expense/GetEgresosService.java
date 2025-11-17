package gastu.gastu.application.service.expense;

import gastu.gastu.application.port.input.expense.GetEgresosByUsuarioUseCase;
import gastu.gastu.application.port.output.EgresoRepositoryPort;
import gastu.gastu.domain.exception.BusinessException;
import gastu.gastu.domain.model.expense.Egreso;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.expense.EgresoResponse;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.ConceptoEgresoEntity;
import gastu.gastu.infrastructure.adapter.output.persistence.repository.JpaConceptoEgresoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para obtener egresos de un usuario
 */
@Service
@RequiredArgsConstructor
public class GetEgresosService implements GetEgresosByUsuarioUseCase {

    private final EgresoRepositoryPort egresoRepository;
    private final JpaConceptoEgresoRepository conceptoRepository;

    /**
     * Obtiene todos los egresos activos de un usuario
     */
    @Override
    @Transactional(readOnly = true)
    public List<EgresoResponse> execute(Long usuarioId) {
        List<Egreso> egresos = egresoRepository.findByUsuarioIdAndActivoTrue(usuarioId);
        return mapToResponseList(egresos);
    }

    /**
     * Obtiene egresos de un usuario en un rango de fechas
     */
    @Transactional(readOnly = true)
    public List<EgresoResponse> getByFechaRange(Long usuarioId, LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio.isAfter(fechaFin)) {
            throw new BusinessException("La fecha de inicio no puede ser posterior a la fecha fin");
        }

        List<Egreso> egresos = egresoRepository.findByUsuarioIdAndFechaTransaccionBetween(
                usuarioId, fechaInicio, fechaFin);
        return mapToResponseList(egresos);
    }

    /**
     * Obtiene todos los egresos de un usuario (incluidos inactivos)
     */
    @Transactional(readOnly = true)
    public List<EgresoResponse> getAllByUsuario(Long usuarioId) {
        List<Egreso> egresos = egresoRepository.findByUsuarioId(usuarioId);
        return mapToResponseList(egresos);
    }

    /**
     * Obtiene un egreso por ID
     */
    @Transactional(readOnly = true)
    public EgresoResponse getById(Long egresoId, Long usuarioId) {
        Egreso egreso = egresoRepository.findById(egresoId)
                .orElseThrow(() -> new BusinessException("Egreso no encontrado con ID: " + egresoId));

        // Verificar que el egreso pertenece al usuario
        if (!egreso.getUsuarioId().equals(usuarioId)) {
            throw new BusinessException("No tienes permiso para ver este egreso");
        }

        ConceptoEgresoEntity concepto = conceptoRepository.findById(egreso.getConceptoEgresoId())
                .orElse(null);

        return mapToResponse(egreso, concepto);
    }

    private List<EgresoResponse> mapToResponseList(List<Egreso> egresos) {
        return egresos.stream()
                .map(egreso -> {
                    ConceptoEgresoEntity concepto = conceptoRepository.findById(egreso.getConceptoEgresoId())
                            .orElse(null);
                    return mapToResponse(egreso, concepto);
                })
                .collect(Collectors.toList());
    }

    private EgresoResponse mapToResponse(Egreso egreso, ConceptoEgresoEntity concepto) {
        EgresoResponse.ConceptoEgresoSimpleResponse conceptoResponse = null;
        if (concepto != null) {
            conceptoResponse = EgresoResponse.ConceptoEgresoSimpleResponse.builder()
                    .conceptoEgresoId(concepto.getConceptoEgresoId())
                    .nombre(concepto.getNombre())
                    .build();
        }

        return EgresoResponse.builder()
                .egresoId(egreso.getEgresoId())
                .monto(egreso.getMonto())
                .descripcion(egreso.getDescripcion())
                .fechaTransaccion(egreso.getFechaTransaccion())
                .fechaRegistro(egreso.getFechaRegistro())
                .activo(egreso.isActivo())
                .concepto(conceptoResponse)
                .build();
    }
}