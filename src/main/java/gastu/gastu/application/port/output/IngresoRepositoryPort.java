package gastu.gastu.application.port.output;

import gastu.gastu.domain.model.income.Ingreso;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para el repositorio de Ingreso
 * Define las operaciones de persistencia sin depender de la implementación
 */
public interface IngresoRepositoryPort {

    /**
     * Guarda un ingreso nuevo o actualiza uno existente
     */
    Ingreso save(Ingreso ingreso);

    /**
     * Busca un ingreso por su ID
     */
    Optional<Ingreso> findById(Long id);

    /**
     * Busca todos los ingresos activos de un usuario
     */
    List<Ingreso> findByUsuarioIdAndActivoTrue(Long usuarioId);

    /**
     * Busca todos los ingresos de un usuario (incluidos inactivos)
     */
    List<Ingreso> findByUsuarioId(Long usuarioId);

    /**
     * Busca ingresos de un usuario en un rango de fechas
     */
    List<Ingreso> findByUsuarioIdAndFechaTransaccionBetween(
            Long usuarioId,
            LocalDate fechaInicio,
            LocalDate fechaFin
    );

    /**
     * Busca ingresos por concepto
     */
    List<Ingreso> findByConceptoIngresoId(Long conceptoIngresoId);

    /**
     * Desactiva todos los ingresos asociados a un concepto
     */
    void desactivarByConceptoId(Long conceptoIngresoId);

    /**
     * Elimina físicamente un ingreso (solo para casos excepcionales)
     */
    void deleteById(Long id);

    /**
     * Verifica si existe un ingreso con el ID especificado
     */
    boolean existsById(Long id);
}