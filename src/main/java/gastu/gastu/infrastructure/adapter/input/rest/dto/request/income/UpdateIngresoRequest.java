package gastu.gastu.infrastructure.adapter.input.rest.dto.request.income;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para actualizar un Ingreso
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateIngresoRequest {

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    @Digits(integer = 10, fraction = 2, message = "El monto debe tener máximo 10 dígitos enteros y 2 decimales")
    private BigDecimal monto;

    @Size(max = 100, message = "La descripción no puede superar los 100 caracteres")
    private String descripcion;

    @NotNull(message = "La fecha de transacción es obligatoria")
    private LocalDate fechaTransaccion;

    @NotNull(message = "El concepto de ingreso es obligatorio")
    private Long conceptoIngresoId;

    private Boolean activo;
}