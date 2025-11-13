package gastu.gastu.domain.model.shared;

/**
 * Enum que define los tipos de transacción
 */
public enum TipoTransaccion {

    /**
     * Para ingresos/egresos recurrentes (fijos mensuales)
     * Ejemplo: Salario, Arriendo, Servicios
     */
    RECURRENTE,

    /**
     * Para ingresos/egresos variables (ocasionales)
     * Ejemplo: Regalo, Café, Comida
     */
    VARIABLE,

    /**
     * Puede usarse en ambos tipos
     * Ejemplo: Freelance, Educación, Salud
     */
    AMBOS;

    /**
     * Verifica si es válido para transacciones recurrentes
     */
    public boolean esValidoParaRecurrente() {
        return this == RECURRENTE || this == AMBOS;
    }

    /**
     * Verifica si es válido para transacciones variables
     */
    public boolean esValidoParaVariable() {
        return this == VARIABLE || this == AMBOS;
    }
}