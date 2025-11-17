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
 * Repositorio JPA para Egreso
 */
@Repository
public interface JpaEgresoRepository extends JpaRepository<EgresoEntity, Long> {

    /**
     * Busca egresos activos de un usuario
     */
    List<EgresoEntity> findByUsuarioUsuarioIdAndActivoTrue(Long usuarioId);

    /**
     * Busca todos los egresos de un usuario
     */
    List<EgresoEntity> findByUsuarioUsuarioId(Long usuarioId);

    /**
     * Busca egresos de un usuario en un rango de fechas
     */
    @Query("SELECT e FROM EgresoEntity e WHERE e.usuario.usuarioId = :usuarioId " +
            "AND e.fechaTransaccion BETWEEN :fechaInicio AND :fechaFin " +
            "ORDER BY e.fechaTransaccion DESC")
    List<EgresoEntity> findByUsuarioAndFechaTransaccionBetween(
            @Param("usuarioId") Long usuarioId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    /**
     * Busca egresos por concepto
     */
    List<EgresoEntity> findByConceptoEgresoConceptoEgresoId(Long conceptoEgresoId);

    /**
     * Desactiva todos los egresos asociados a un concepto
     */
    @Modifying
    @Query("UPDATE EgresoEntity e SET e.activo = false WHERE e.conceptoEgreso.conceptoEgresoId = :conceptoId")
    void desactivarByConceptoId(@Param("conceptoId") Long conceptoId);
}