package gastu.gastu.infrastructure.adapter.output.persistence.repository;

import gastu.gastu.infrastructure.adapter.output.persistence.entity.EgresoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio JPA para Egresos
 */
@Repository
public interface JpaEgresoRepository extends JpaRepository<EgresoEntity, Long> {

    /**
     * Busca egresos activos de un usuario
     */
    List<EgresoEntity> findByUsuarioUsuarioIdAndActivoTrue(Long usuarioId);

    /**
     * Busca todos los egresos de un usuario (activos e inactivos)
     */
    List<EgresoEntity> findByUsuarioUsuarioId(Long usuarioId);

    /**
     * Busca egresos de un usuario en un rango de fechas
     */
    @Query("SELECT e FROM EgresoEntity e WHERE e.usuario.usuarioId = :usuarioId " +
            "AND e.fechaTransaccion BETWEEN :fechaInicio AND :fechaFin " +
            "AND e.activo = true " +
            "ORDER BY e.fechaTransaccion DESC")
    List<EgresoEntity> findByUsuarioAndFechaRange(
            @Param("usuarioId") Long usuarioId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    /**
     * Busca egresos por concepto
     */
    List<EgresoEntity> findByConceptoEgresoConceptoEgresoIdAndActivoTrue(Long conceptoId);

    /**
     * Desactiva todos los egresos de un concepto (soft delete en cascada)
     */
    @Modifying
    @Query("UPDATE EgresoEntity e SET e.activo = false " +
            "WHERE e.conceptoEgreso.conceptoEgresoId = :conceptoId")
    void desactivarByConceptoId(@Param("conceptoId") Long conceptoId);

    /**
     * Cuenta egresos activos de un usuario
     */
    long countByUsuarioUsuarioIdAndActivoTrue(Long usuarioId);

    /**
     * Busca egresos del mes actual de un usuario
     */
    @Query("SELECT e FROM EgresoEntity e WHERE e.usuario.usuarioId = :usuarioId " +
            "AND YEAR(e.fechaTransaccion) = :anio " +
            "AND MONTH(e.fechaTransaccion) = :mes " +
            "AND e.activo = true " +
            "ORDER BY e.fechaTransaccion DESC")
    List<EgresoEntity> findByUsuarioAndMes(
            @Param("usuarioId") Long usuarioId,
            @Param("mes") int mes,
            @Param("anio") int anio
    );
}
