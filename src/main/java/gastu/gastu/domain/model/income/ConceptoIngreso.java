package gastu.gastu.domain.model.income;

import gastu.gastu.domain.model.shared.TipoTransaccion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad de dominio que representa un Concepto de Ingreso
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConceptoIngreso {

    private Long conceptoIngresoId;
    private String nombre;
    private String descripcion;
    private TipoTransaccion tipoTransaccion;
    private boolean activo;
    private LocalDateTime fechaCreacion;

    /**
     * Verifica si el concepto puede usarse para ingresos recurrentes
     */
    public boolean esValidoParaRecurrente() {
        return tipoTransaccion == TipoTransaccion.RECURRENTE
                || tipoTransaccion == TipoTransaccion.AMBOS;
    }

    /**
     * Verifica si el concepto puede usarse para ingresos variables
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