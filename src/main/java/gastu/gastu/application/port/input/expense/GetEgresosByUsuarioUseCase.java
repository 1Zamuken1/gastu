package gastu.gastu.application.port.input.expense;

import gastu.gastu.infrastructure.adapter.input.rest.dto.response.expense.EgresoResponse;

import java.util.List;

/**
 * Puerto de entrada para el caso de uso de Obtener Egresos de un Usuario
 */
public interface GetEgresosByUsuarioUseCase {

    /**
     * Obtiene todos los egresos activos de un usuario
     *
     * @param usuarioId ID del usuario
     * @return Lista de egresos activos
     */
    List<EgresoResponse> execute(Long usuarioId);
}