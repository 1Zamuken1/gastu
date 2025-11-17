package gastu.gastu.domain.model.shared;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Enum que define las frecuencias de recurrencia para proyecciones
 */
public enum Frecuencia {

    /**
     * Cada día
     */
    DIARIA(1, ChronoUnit.DAYS),

    /**
     * Cada 7 días
     */
    SEMANAL(7, ChronoUnit.DAYS),

    /**
     * Cada 15 días
     */
    QUINCENAL(15, ChronoUnit.DAYS),

    /**
     * Cada mes (mismo día)
     */
    MENSUAL(1, ChronoUnit.MONTHS),

    /**
     * Cada 2 meses
     */
    BIMESTRAL(2, ChronoUnit.MONTHS),

    /**
     * Cada 3 meses
     */
    TRIMESTRAL(3, ChronoUnit.MONTHS),

    /**
     * Cada 6 meses
     */
    SEMESTRAL(6, ChronoUnit.MONTHS),

    /**
     * Cada año
     */
    ANUAL(1, ChronoUnit.YEARS);

    private final int cantidad;
    private final ChronoUnit unidad;

    Frecuencia(int cantidad, ChronoUnit unidad) {
        this.cantidad = cantidad;
        this.unidad = unidad;
    }

    /**
     * Calcula la próxima fecha basada en una fecha dada
     *
     * @param fechaBase Fecha desde la cual calcular
     * @return La próxima fecha según la frecuencia
     */
    public LocalDate calcularProximaFecha(LocalDate fechaBase) {
        return fechaBase.plus(cantidad, unidad);
    }

    /**
     * Verifica si una fecha ya está vencida según la frecuencia
     *
     * @param fechaEsperada Fecha en que se esperaba la confirmación
     * @param fechaActual Fecha actual
     * @return true si está vencida
     */
    public boolean estaVencida(LocalDate fechaEsperada, LocalDate fechaActual) {
        return fechaActual.isAfter(fechaEsperada);
    }

    public int getCantidad() {
        return cantidad;
    }

    public ChronoUnit getUnidad() {
        return unidad;
    }
}