package gastu.gastu.infrastructure.adapter.output.persistence.repository;

import gastu.gastu.infrastructure.adapter.output.persistence.entity.IngresoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio JPA para Ingresos
 */
@Repository
public interface JpaIngresoRepository extends JpaRepository<IngresoEntity, Long> {

    /**
     * Busca ingresos activos de un usuario
     */
    List<IngresoEntity> findByUsuarioUsuarioIdAndActivoTrue(Long usuarioId);

    /**
     * Busca todos los ingresos de un usuario (activos e inactivos)
     */
    List<IngresoEntity> findByUsuarioUsuarioId(Long usuarioId);

    /**
     * Busca ingresos de un usuario en un rango de fechas
     */
    @Query("SELECT i FROM IngresoEntity i WHERE i.usuario.usuarioId = :usuarioId " +
            "AND i.fechaTransaccion BETWEEN :fechaInicio AND :fechaFin " +
            "AND i.activo = true " +
            "ORDER BY i.fechaTransaccion DESC")
    List<IngresoEntity> findByUsuarioAndFechaRange(
            @Param("usuarioId") Long usuarioId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    /**
     * Busca ingresos por concepto
     */
    List<IngresoEntity> findByConceptoIngresoConceptoIngresoIdAndActivoTrue(Long conceptoId);

    /**
     * Desactiva todos los ingresos de un concepto (soft delete en cascada)
     */
    @Modifying
    @Query("UPDATE IngresoEntity i SET i.activo = false " +
            "WHERE i.conceptoIngreso.conceptoIngresoId = :conceptoId")
    void desactivarByConceptoId(@Param("conceptoId") Long conceptoId);

    /**
     * Cuenta ingresos activos de un usuario
     */
    long countByUsuarioUsuarioIdAndActivoTrue(Long usuarioId);

    /**
     * Busca ingresos del mes actual de un usuario
     */
    @Query("SELECT i FROM IngresoEntity i WHERE i.usuario.usuarioId = :usuarioId " +
            "AND YEAR(i.fechaTransaccion) = :anio " +
            "AND MONTH(i.fechaTransaccion) = :mes " +
            "AND i.activo = true " +
            "ORDER BY i.fechaTransaccion DESC")
    List<IngresoEntity> findByUsuarioAndMes(
            @Param("usuarioId") Long usuarioId,
            @Param("mes") int mes,
            @Param("anio") int anio
    );
}