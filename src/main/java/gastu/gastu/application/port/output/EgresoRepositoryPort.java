package gastu.gastu.application.port.output;

import gastu.gastu.domain.model.expense.Egreso;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para el repositorio de Egreso
 * Define las operaciones de persistencia sin depender de la implementación
 */
public interface EgresoRepositoryPort {

    /**
     * Guarda un egreso nuevo o actualiza uno existente
     */
    Egreso save(Egreso egreso);

    /**
     * Busca un egreso por su ID
     */
    Optional<Egreso> findById(Long id);

    /**
     * Busca todos los egresos activos de un usuario
     */
    List<Egreso> findByUsuarioIdAndActivoTrue(Long usuarioId);

    /**
     * Busca todos los egresos de un usuario (incluidos inactivos)
     */
    List<Egreso> findByUsuarioId(Long usuarioId);

    /**
     * Busca egresos de un usuario en un rango de fechas
     */
    List<Egreso> findByUsuarioIdAndFechaTransaccionBetween(
            Long usuarioId,
            LocalDate fechaInicio,
            LocalDate fechaFin
    );

    /**
     * Busca egresos por concepto
     */
    List<Egreso> findByConceptoEgresoId(Long conceptoEgresoId);

    /**
     * Desactiva todos los egresos asociados a un concepto
     */
    void desactivarByConceptoId(Long conceptoEgresoId);

    /**
     * Elimina físicamente un egreso (solo para casos excepcionales)
     */
    void deleteById(Long id);

    /**
     * Verifica si existe un egreso con el ID especificado
     */
    boolean existsById(Long id);
}