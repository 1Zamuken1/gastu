package gastu.gastu.application.port.input.income;

import gastu.gastu.infrastructure.adapter.input.rest.dto.request.income.UpdateIngresoRequest;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.income.IngresoResponse;

/**
 * Puerto de entrada para el caso de uso de Actualizar Ingreso
 */
public interface UpdateIngresoUseCase {

    /**
     * Actualiza un ingreso existente
     *
     * @param ingresoId ID del ingreso a actualizar
     * @param request Nuevos datos del ingreso
     * @param usuarioId ID del usuario que actualiza (para verificar permisos)
     * @return IngresoResponse con los datos actualizados
     * @throws gastu.gastu.domain.exception.BusinessException si el ingreso no existe o no pertenece al usuario
     */
    IngresoResponse execute(Long ingresoId, UpdateIngresoRequest request, Long usuarioId);
}