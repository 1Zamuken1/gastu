package gastu.gastu.application.port.input.expense;

import gastu.gastu.infrastructure.adapter.input.rest.dto.request.expense.UpdateEgresoRequest;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.expense.EgresoResponse;

/**
 * Puerto de entrada para el caso de uso de Actualizar Egreso
 */
public interface UpdateEgresoUseCase {

    /**
     * Actualiza un egreso existente
     *
     * @param egresoId ID del egreso a actualizar
     * @param request Nuevos datos del egreso
     * @param usuarioId ID del usuario que actualiza (para verificar permisos)
     * @return EgresoResponse con los datos actualizados
     * @throws gastu.gastu.domain.exception.BusinessException si el egreso no existe o no pertenece al usuario
     */
    EgresoResponse execute(Long egresoId, UpdateEgresoRequest request, Long usuarioId);
}