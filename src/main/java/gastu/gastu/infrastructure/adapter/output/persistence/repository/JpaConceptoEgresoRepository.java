package gastu.gastu.infrastructure.adapter.output.persistence.repository;

import gastu.gastu.domain.model.shared.TipoTransaccion;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.ConceptoEgresoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para ConceptoEgreso
 */
@Repository
public interface JpaConceptoEgresoRepository extends JpaRepository<ConceptoEgresoEntity, Long> {

    /**
     * Busca conceptos activos
     */
    List<ConceptoEgresoEntity> findByActivoTrue();

    /**
     * Busca conceptos por tipo de transacción
     */
    List<ConceptoEgresoEntity> findByTipoTransaccionAndActivoTrue(TipoTransaccion tipoTransaccion);

    /**
     * Busca conceptos válidos para egresos recurrentes (RECURRENTE o AMBOS)
     */
    @Query("SELECT c FROM ConceptoEgresoEntity c WHERE (c.tipoTransaccion = 'RECURRENTE' OR c.tipoTransaccion = 'AMBOS') AND c.activo = true")
    List<ConceptoEgresoEntity> findValidosParaRecurrente();

    /**
     * Busca conceptos válidos para egresos variables (VARIABLE o AMBOS)
     */
    @Query("SELECT c FROM ConceptoEgresoEntity c WHERE (c.tipoTransaccion = 'VARIABLE' OR c.tipoTransaccion = 'AMBOS') AND c.activo = true")
    List<ConceptoEgresoEntity> findValidosParaVariable();

    /**
     * Verifica si existe un concepto con el mismo nombre
     */
    boolean existsByNombreIgnoreCase(String nombre);
}