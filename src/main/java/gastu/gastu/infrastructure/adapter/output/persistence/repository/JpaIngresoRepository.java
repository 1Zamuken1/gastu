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
 * Repositorio JPA para Ingreso
 */
@Repository
public interface JpaIngresoRepository extends JpaRepository<IngresoEntity, Long> {

    /**
     * Busca ingresos activos de un usuario
     */
    List<IngresoEntity> findByUsuarioUsuarioIdAndActivoTrue(Long usuarioId);

    /**
     * Busca todos los ingresos de un usuario
     */
    List<IngresoEntity> findByUsuarioUsuarioId(Long usuarioId);

    /**
     * Busca ingresos de un usuario en un rango de fechas
     */
    @Query("SELECT i FROM IngresoEntity i WHERE i.usuario.usuarioId = :usuarioId " +
            "AND i.fechaTransaccion BETWEEN :fechaInicio AND :fechaFin " +
            "ORDER BY i.fechaTransaccion DESC")
    List<IngresoEntity> findByUsuarioAndFechaTransaccionBetween(
            @Param("usuarioId") Long usuarioId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    /**
     * Busca ingresos por concepto
     */
    List<IngresoEntity> findByConceptoIngresoConceptoIngresoId(Long conceptoIngresoId);

    /**
     * Desactiva todos los ingresos asociados a un concepto
     */
    @Modifying
    @Query("UPDATE IngresoEntity i SET i.activo = false WHERE i.conceptoIngreso.conceptoIngresoId = :conceptoId")
    void desactivarByConceptoId(@Param("conceptoId") Long conceptoId);
}