package gastu.gastu.infrastructure.adapter.input.rest.dto.response.expense;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para Egreso
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EgresoResponse {

    private Long egresoId;
    private BigDecimal monto;
    private String descripcion;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaTransaccion;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaRegistro;

    private boolean activo;

    // Información del concepto (simplificada)
    private ConceptoEgresoSimpleResponse concepto;

    // ⭐ NUEVO: Alerta si el egreso genera problemas financieros
    private gastu.gastu.infrastructure.adapter.input.rest.dto.response.shared.AlertaResponse alerta;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConceptoEgresoSimpleResponse {
        private Long conceptoEgresoId;
        private String nombre;
    }
}