package gastu.gastu.application.port.input.income;

import gastu.gastu.infrastructure.adapter.input.rest.dto.request.income.CreateIngresoRequest;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.income.IngresoResponse;

public interface CreateIngresoUseCase {
    IngresoResponse execute(CreateIngresoRequest request, Long usuarioId);
}