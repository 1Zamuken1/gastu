package gastu.gastu.application.port.input.expense;

import gastu.gastu.infrastructure.adapter.input.rest.dto.request.expense.UpdateEgresoRequest;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.expense.EgresoResponse;

public interface UpdateEgresoUseCase {
    EgresoResponse execute(Long egresoId, UpdateEgresoRequest request, Long usuarioId);
}