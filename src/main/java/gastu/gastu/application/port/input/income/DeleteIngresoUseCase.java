package gastu.gastu.application.port.input.income;

public interface DeleteIngresoUseCase {
    void desactivar(Long ingresoId, Long usuarioId);
    void eliminar(Long ingresoId, Long usuarioId);
}