package gastu.gastu.domain.model.shared;

public enum TipoProyeccion {
    INGRESO,
    EGRESO;

    public boolean esIngreso() {
        return this == INGRESO;
    }

    public boolean esEgreso() {
        return this == EGRESO;
    }
}