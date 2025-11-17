package gastu.gastu.domain.model.income;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad de dominio que representa un Ingreso real del usuario
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ingreso {

    private Long ingresoId;
    private BigDecimal monto;
    private String descripcion;
    private LocalDateTime fechaRegistro;
    private LocalDate fechaTransaccion;
    private boolean activo;

    // Relaciones
    private Long conceptoIngresoId;
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
     * Desactiva el ingreso (soft delete)
     */
    public void desactivar() {
        this.activo = false;
    }

    /**
     * Activa el ingreso
     */
    public void activar() {
        this.activo = true;
    }

    /**
     * Valida que el ingreso sea válido para ser creado/actualizado
     */
    public boolean isValido() {
        return isMontoValido()
                && isFechaTransaccionValida()
                && conceptoIngresoId != null
                && usuarioId != null;
    }
}