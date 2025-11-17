package gastu.gastu.application.port.input.expense;

import gastu.gastu.infrastructure.adapter.input.rest.dto.request.expense.CreateEgresoRequest;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.expense.EgresoResponse;

/**
 * Puerto de entrada para el caso de uso de Crear Egreso
 */
public interface CreateEgresoUseCase {

    /**
     * Crea un nuevo egreso para un usuario
     *
     * @param request Datos del egreso a crear
     * @param usuarioId ID del usuario que registra el egreso
     * @return EgresoResponse con los datos del egreso creado
     * @throws gastu.gastu.domain.exception.BusinessException si el concepto no existe o está inactivo
     */
    EgresoResponse execute(CreateEgresoRequest request, Long usuarioId);
}