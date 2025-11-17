package gastu.gastu.application.port.input.income;

/**
 * Puerto de entrada para el caso de uso de Eliminar Ingreso
 */
public interface DeleteIngresoUseCase {

    /**
     * Desactiva un ingreso (soft delete)
     *
     * @param ingresoId ID del ingreso a desactivar
     * @param usuarioId ID del usuario (para verificar permisos)
     * @throws gastu.gastu.domain.exception.BusinessException si el ingreso no existe o no pertenece al usuario
     */
    void softDelete(Long ingresoId, Long usuarioId);
}