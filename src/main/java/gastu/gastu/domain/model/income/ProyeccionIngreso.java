package gastu.gastu.domain.model.income;

import gastu.gastu.domain.model.shared.Frecuencia;
import gastu.gastu.domain.model.user.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad de dominio que representa una Proyección de Ingreso (recurrente)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProyeccionIngreso {

    private Long proyeccionIngresoId;
    private BigDecimal montoProgramado;
    private String descripcion;
    private Frecuencia frecuencia;
    private LocalDate fechaFin;
    private boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private ConceptoIngreso conceptoIngreso;
    private Usuario usuario;

    /**
     * Valida que el monto programado sea positivo
     */
    public boolean esMontoValido() {
        return montoProgramado != null && montoProgramado.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Valida que tenga descripción (obligatoria en proyecciones)
     */
    public boolean tieneDescripcion() {
        return descripcion != null && !descripcion.trim().isEmpty();
    }

    /**
     * Valida que el concepto sea válido para proyecciones (RECURRENTE o AMBOS)
     */
    public boolean esConceptoValido() {
        return conceptoIngreso != null && conceptoIngreso.esValidoParaRecurrente();
    }

    /**
     * Calcula la próxima fecha de confirmación basada en una fecha dada
     */
    public LocalDate calcularProximaFecha(LocalDate fechaBase) {
        if (frecuencia == null) {
            throw new IllegalStateException("No se puede calcular próxima fecha sin frecuencia definida");
        }
        return frecuencia.calcularProximaFecha(fechaBase);
    }

    /**
     * Verifica si la proyección está vigente (activa y no vencida)
     */
    public boolean estaVigente() {
        if (!activo) {
            return false;
        }

        if (fechaFin == null) {
            return true; // Sin fecha fin = indefinida
        }

        return !LocalDate.now().isAfter(fechaFin);
    }

    /**
     * Verifica si la proyección ya venció por fecha fin
     */
    public boolean estaVencida() {
        return fechaFin != null && LocalDate.now().isAfter(fechaFin);
    }

    /**
     * Verifica si debe generar una nueva confirmación para una fecha dada
     */
    public boolean debeGenerarConfirmacion(LocalDate fechaEsperada) {
        if (!estaVigente()) {
            return false;
        }

        LocalDate hoy = LocalDate.now();

        // Si la fecha esperada es hoy o pasada, debe generarse
        return !fechaEsperada.isAfter(hoy);
    }

    /**
     * Desactiva la proyección (soft delete)
     */
    public void desactivar() {
        this.activo = false;
    }

    /**
     * Activa la proyección
     */
    public void activar() {
        this.activo = true;
    }

    /**
     * Pausa la proyección temporalmente
     */
    public void pausar() {
        this.activo = false;
    }

    /**
     * Verifica si es indefinida (sin fecha fin)
     */
    public boolean esIndefinida() {
        return fechaFin == null;
    }
}