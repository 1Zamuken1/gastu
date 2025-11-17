package gastu.gastu.application.port.input.income;

import gastu.gastu.infrastructure.adapter.input.rest.dto.request.income.UpdateIngresoRequest;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.income.IngresoResponse;

public interface UpdateIngresoUseCase {
    IngresoResponse execute(Long ingresoId, UpdateIngresoRequest request, Long usuarioId);
}