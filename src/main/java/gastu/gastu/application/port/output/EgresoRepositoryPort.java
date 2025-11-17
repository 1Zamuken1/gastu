package gastu.gastu.application.port.output;

import gastu.gastu.domain.model.expense.Egreso;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EgresoRepositoryPort {

    Egreso save(Egreso egreso);

    Optional<Egreso> findById(Long id);

    List<Egreso> findActivosByUsuario(Long usuarioId);

    List<Egreso> findAllByUsuario(Long usuarioId);

    List<Egreso> findByUsuarioAndFechaRange(Long usuarioId, LocalDate fechaInicio, LocalDate fechaFin);

    List<Egreso> findByUsuarioAndMes(Long usuarioId, int mes, int anio);

    void delete(Long id);

    long countActivosByUsuario(Long usuarioId);

    List<Egreso> findByUsuarioId(Long usuarioId);
}