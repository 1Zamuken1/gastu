package gastu.gastu.application.port.input.expense;

import gastu.gastu.infrastructure.adapter.input.rest.dto.request.expense.CreateEgresoRequest;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.expense.EgresoResponse;

public interface CreateEgresoUseCase {
    EgresoResponse execute(CreateEgresoRequest request, Long usuarioId);
}