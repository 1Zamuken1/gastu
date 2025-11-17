package gastu.gastu.domain.model.shared;

public enum EstadoConfirmacion {
    PENDIENTE,
    CONFIRMADA,
    CANCELADA;

    public boolean isPendiente() {
        return this == PENDIENTE;
    }

    public boolean estaProcesada() {
        return this == CONFIRMADA || this == CANCELADA;
    }
}