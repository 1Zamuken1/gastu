package gastu.gastu.application.port.input.income;

import gastu.gastu.infrastructure.adapter.input.rest.dto.response.income.IngresoResponse;

import java.util.List;

/**
 * Puerto de entrada para el caso de uso de Obtener Ingresos de un Usuario
 */
public interface GetIngresosByUsuarioUseCase {

    /**
     * Obtiene todos los ingresos activos de un usuario
     *
     * @param usuarioId ID del usuario
     * @return Lista de ingresos activos
     */
    List<IngresoResponse> execute(Long usuarioId);
}