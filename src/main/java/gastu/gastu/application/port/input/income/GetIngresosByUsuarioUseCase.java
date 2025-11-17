package gastu.gastu.application.port.input.income;

import gastu.gastu.infrastructure.adapter.input.rest.dto.response.income.IngresoResponse;
import java.time.LocalDate;
import java.util.List;

public interface GetIngresosByUsuarioUseCase {
    List<IngresoResponse> getAllActivos(Long usuarioId);
    List<IngresoResponse> getAll(Long usuarioId);
    List<IngresoResponse> getByFechaRange(Long usuarioId, LocalDate fechaInicio, LocalDate fechaFin);
    List<IngresoResponse> getByMes(Long usuarioId, int mes, int anio);
    IngresoResponse getById(Long ingresoId, Long usuarioId);
}