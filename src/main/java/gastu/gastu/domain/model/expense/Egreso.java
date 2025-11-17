package gastu.gastu.domain.model.expense;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad de dominio que representa un Egreso real del usuario
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Egreso {

    private Long egresoId;
    private BigDecimal monto;
    private String descripcion;
    private LocalDateTime fechaRegistro;
    private LocalDate fechaTransaccion;
    private boolean activo;

    // Relaciones
    private Long conceptoEgresoId;
    private Long usuarioId;

    /**
     * Valida que el monto sea positivo
     */
    public boolean isMontoValido() {
        return monto != null && monto.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Valida que la fecha de transacción no sea nula
     */
    public boolean isFechaTransaccionValida() {
        return fechaTransaccion != null;
    }

    /**
     * Desactiva el egreso (soft delete)
     */
    public void desactivar() {
        this.activo = false;
    }

    /**
     * Activa el egreso
     */
    public void activar() {
        this.activo = true;
    }

    /**
     * Valida que el egreso sea válido para ser creado/actualizado
     */
    public boolean isValido() {
        return isMontoValido()
                && isFechaTransaccionValida()
                && conceptoEgresoId != null
                && usuarioId != null;
    }
}