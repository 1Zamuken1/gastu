package gastu.gastu.domain.model.expense;

import gastu.gastu.domain.model.user.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private ConceptoEgreso conceptoEgreso;
    private Usuario usuario;

    public boolean esMontoValido() {
        return monto != null && monto.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean esFechaValida() {
        return fechaTransaccion != null;
    }

    public boolean esDelAnioActual() {
        return fechaTransaccion.getYear() == LocalDate.now().getYear();
    }

    public boolean esDelMes(int mes, int anio) {
        return fechaTransaccion.getMonthValue() == mes
                && fechaTransaccion.getYear() == anio;
    }

    public void desactivar() {
        this.activo = false;
    }

    public void activar() {
        this.activo = true;
    }

    public boolean tieneDescripcion() {
        return descripcion != null && !descripcion.trim().isEmpty();
    }
}