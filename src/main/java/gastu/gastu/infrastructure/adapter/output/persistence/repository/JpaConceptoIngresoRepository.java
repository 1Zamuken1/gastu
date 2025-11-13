package gastu.gastu.infrastructure.adapter.output.persistence.repository;

import gastu.gastu.domain.model.shared.TipoTransaccion;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.ConceptoIngresoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para ConceptoIngreso
 */
@Repository
public interface JpaConceptoIngresoRepository extends JpaRepository<ConceptoIngresoEntity, Long> {

    /**
     * Busca conceptos activos
     */
    List<ConceptoIngresoEntity> findByActivoTrue();

    /**
     * Busca conceptos por tipo de transacción
     */
    List<ConceptoIngresoEntity> findByTipoTransaccionAndActivoTrue(TipoTransaccion tipoTransaccion);

    /**
     * Busca conceptos válidos para ingresos recurrentes (RECURRENTE o AMBOS)
     */
    @Query("SELECT c FROM ConceptoIngresoEntity c WHERE (c.tipoTransaccion = 'RECURRENTE' OR c.tipoTransaccion = 'AMBOS') AND c.activo = true")
    List<ConceptoIngresoEntity> findValidosParaRecurrente();

    /**
     * Busca conceptos válidos para ingresos variables (VARIABLE o AMBOS)
     */
    @Query("SELECT c FROM ConceptoIngresoEntity c WHERE (c.tipoTransaccion = 'VARIABLE' OR c.tipoTransaccion = 'AMBOS') AND c.activo = true")
    List<ConceptoIngresoEntity> findValidosParaVariable();

    /**
     * Verifica si existe un concepto con el mismo nombre
     */
    boolean existsByNombreIgnoreCase(String nombre);
}