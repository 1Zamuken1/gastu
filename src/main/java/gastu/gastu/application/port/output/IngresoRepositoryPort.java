package gastu.gastu.application.port.output;

import gastu.gastu.domain.model.income.Ingreso;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para el repositorio de Ingreso
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
     * Busca ingresos activos de un usuario
     */
    List<Ingreso> findActivosByUsuario(Long usuarioId);

    /**
     * Busca todos los ingresos de un usuario (activos e inactivos)
     */
    List<Ingreso> findAllByUsuario(Long usuarioId);

    /**
     * Busca ingresos en un rango de fechas
     */
    List<Ingreso> findByUsuarioAndFechaRange(Long usuarioId, LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Busca ingresos de un mes específico
     */
    List<Ingreso> findByUsuarioAndMes(Long usuarioId, int mes, int anio);

    /**
     * Elimina un ingreso (puede ser físico o lógico)
     */
    void delete(Long id);

    /**
     * Cuenta ingresos activos de un usuario
     */
    long countActivosByUsuario(Long usuarioId);
}