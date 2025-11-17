package gastu.gastu.application.port.input.expense;

public interface DeleteEgresoUseCase {
    void desactivar(Long egresoId, Long usuarioId);
    void eliminar(Long egresoId, Long usuarioId);
}