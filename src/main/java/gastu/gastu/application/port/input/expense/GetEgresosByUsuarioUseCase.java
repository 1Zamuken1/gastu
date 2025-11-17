package gastu.gastu.application.port.input.expense;

import gastu.gastu.infrastructure.adapter.input.rest.dto.response.expense.EgresoResponse;
import java.time.LocalDate;
import java.util.List;

public interface GetEgresosByUsuarioUseCase {
    List<EgresoResponse> getAllActivos(Long usuarioId);
    List<EgresoResponse> getAll(Long usuarioId);
    List<EgresoResponse> getByFechaRange(Long usuarioId, LocalDate fechaInicio, LocalDate fechaFin);
    List<EgresoResponse> getByMes(Long usuarioId, int mes, int anio);
    EgresoResponse getById(Long egresoId, Long usuarioId);
}