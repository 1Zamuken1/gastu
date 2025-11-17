package gastu.gastu.infrastructure.adapter.input.rest.dto.response.income;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para Ingreso
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngresoResponse {

    private Long ingresoId;
    private BigDecimal monto;
    private String descripcion;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaTransaccion;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaRegistro;

    private boolean activo;

    // Información del concepto (simplificada)
    private ConceptoIngresoSimpleResponse concepto;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConceptoIngresoSimpleResponse {
        private Long conceptoIngresoId;
        private String nombre;
    }
}