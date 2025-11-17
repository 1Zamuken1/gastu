package gastu.gastu.application.port.input.income;

import gastu.gastu.infrastructure.adapter.input.rest.dto.request.income.CreateIngresoRequest;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.income.IngresoResponse;

/**
 * Puerto de entrada para el caso de uso de Crear Ingreso
 */
public interface CreateIngresoUseCase {

    /**
     * Crea un nuevo ingreso para un usuario
     *
     * @param request Datos del ingreso a crear
     * @param usuarioId ID del usuario que registra el ingreso
     * @return IngresoResponse con los datos del ingreso creado
     * @throws gastu.gastu.domain.exception.BusinessException si el concepto no existe o está inactivo
     */
    IngresoResponse execute(CreateIngresoRequest request, Long usuarioId);
}