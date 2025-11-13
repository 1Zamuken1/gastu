package gastu.gastu.domain.model.expense;

import gastu.gastu.domain.model.shared.TipoTransaccion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad de dominio que representa un Concepto de Egreso
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConceptoEgreso {

    private Long conceptoEgresoId;
    private String nombre;
    private String descripcion;
    private TipoTransaccion tipoTransaccion;
    private boolean activo;
    private LocalDateTime fechaCreacion;

    /**
     * Verifica si el concepto puede usarse para egresos recurrentes
     */
    public boolean esValidoParaRecurrente() {
        return tipoTransaccion == TipoTransaccion.RECURRENTE
                || tipoTransaccion == TipoTransaccion.AMBOS;
    }

    /**
     * Verifica si el concepto puede usarse para egresos variables
     */
    public boolean esValidoParaVariable() {
        return tipoTransaccion == TipoTransaccion.VARIABLE
                || tipoTransaccion == TipoTransaccion.AMBOS;
    }

    /**
     * Desactiva el concepto (soft delete)
     */
    public void desactivar() {
        this.activo = false;
    }

    /**
     * Activa el concepto
     */
    public void activar() {
        this.activo = true;
    }
}