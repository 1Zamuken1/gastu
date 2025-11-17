package gastu.gastu.application.port.input.expense;

/**
 * Puerto de entrada para el caso de uso de Eliminar Egreso
 */
public interface DeleteEgresoUseCase {

    /**
     * Desactiva un egreso (soft delete)
     *
     * @param egresoId ID del egreso a desactivar
     * @param usuarioId ID del usuario (para verificar permisos)
     * @throws gastu.gastu.domain.exception.BusinessException si el egreso no existe o no pertenece al usuario
     */
    void softDelete(Long egresoId, Long usuarioId);
}