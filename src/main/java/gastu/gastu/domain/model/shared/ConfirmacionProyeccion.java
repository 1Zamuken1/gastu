package gastu.gastu.domain.model.shared;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad de dominio que representa una Confirmación de Proyección
 *
 * IMPORTANTE: Requiere que existan los enums TipoProyeccion y EstadoConfirmacion
 * en el mismo paquete (gastu.gastu.domain.model.shared)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmacionProyeccion {

    private Long confirmacionId;
    private Long proyeccionId; // ID de la proyección (ingreso o egreso)
    private TipoProyeccion tipoProyeccion; // INGRESO o EGRESO
    private LocalDate fechaEsperada;
    private LocalDateTime fechaConfirmacion;
    private EstadoConfirmacion estado;
    private BigDecimal montoOriginal;
    private BigDecimal montoAjustado;
    private String descripcionOriginal;
    private String descripcionAjustada;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    /**
     * Obtiene el monto final (ajustado si existe, original si no)
     */
    public BigDecimal getMontoFinal() {
        return montoAjustado != null ? montoAjustado : montoOriginal;
    }

    /**
     * Obtiene la descripción final (ajustada si existe, original si no)
     */
    public String getDescripcionFinal() {
        return descripcionAjustada != null ? descripcionAjustada : descripcionOriginal;
    }

    /**
     * Verifica si hubo ajustes en la confirmación
     */
    public boolean tuvoAjustes() {
        return montoAjustado != null || descripcionAjustada != null;
    }

    /**
     * Confirma la proyección con los datos originales
     */
    public void confirmar() {
        if (estado != EstadoConfirmacion.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden confirmar proyecciones PENDIENTES");
        }
        this.estado = EstadoConfirmacion.CONFIRMADA;
        this.fechaConfirmacion = LocalDateTime.now();
    }

    /**
     * Confirma la proyección con ajustes
     */
    public void confirmarConAjustes(BigDecimal montoAjustado, String descripcionAjustada) {
        if (estado != EstadoConfirmacion.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden confirmar proyecciones PENDIENTES");
        }

        if (montoAjustado != null && montoAjustado.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto ajustado debe ser positivo");
        }

        this.montoAjustado = montoAjustado;
        this.descripcionAjustada = descripcionAjustada;
        this.estado = EstadoConfirmacion.CONFIRMADA;
        this.fechaConfirmacion = LocalDateTime.now();
    }

    /**
     * Cancela la confirmación
     */
    public void cancelar() {
        if (estado != EstadoConfirmacion.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden cancelar proyecciones PENDIENTES");
        }
        this.estado = EstadoConfirmacion.CANCELADA;
        this.fechaConfirmacion = LocalDateTime.now();
    }

    /**
     * Verifica si está vencida (fecha esperada pasada y aún pendiente)
     */
    public boolean estaVencida() {
        return estado == EstadoConfirmacion.PENDIENTE
                && LocalDate.now().isAfter(fechaEsperada);
    }

    /**
     * Calcula los días de retraso
     */
    public long diasDeRetraso() {
        if (!estaVencida()) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(fechaEsperada, LocalDate.now());
    }

    /**
     * Verifica si está pendiente
     */
    public boolean estaPendiente() {
        return estado == EstadoConfirmacion.PENDIENTE;
    }

    /**
     * Verifica si fue confirmada
     */
    public boolean fueConfirmada() {
        return estado == EstadoConfirmacion.CONFIRMADA;
    }

    /**
     * Verifica si fue cancelada
     */
    public boolean fueCancelada() {
        return estado == EstadoConfirmacion.CANCELADA;
    }
}