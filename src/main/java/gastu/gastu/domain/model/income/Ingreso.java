package gastu.gastu.domain.model.income;

import gastu.gastu.domain.model.user.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad de dominio que representa un Ingreso real
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
    private ConceptoIngreso conceptoIngreso;
    private Usuario usuario;

    /**
     * Valida que el monto sea positivo
     */
    public boolean esMontoValido() {
        return monto != null && monto.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Valida que la fecha de transacción no sea nula
     */
    public boolean esFechaValida() {
        return fechaTransaccion != null;
    }

    /**
     * Verifica si el ingreso es del año en curso
     */
    public boolean esDelAnioActual() {
        return fechaTransaccion.getYear() == LocalDate.now().getYear();
    }

    /**
     * Verifica si el ingreso es del mes especificado
     */
    public boolean esDelMes(int mes, int anio) {
        return fechaTransaccion.getMonthValue() == mes
                && fechaTransaccion.getYear() == anio;
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
     * Verifica si tiene una descripción
     */
    public boolean tieneDescripcion() {
        return descripcion != null && !descripcion.trim().isEmpty();
    }
}