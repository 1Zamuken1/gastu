package gastu.gastu.infrastructure.adapter.input.rest.dto.response.shared;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO de respuesta para alertas financieras
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertaResponse {

    private String tipo; // SALDO_NEGATIVO, PROXIMO_LIMITE, etc.
    private String titulo;
    private String mensaje;
    private BigDecimal balanceMes;
    private String severidad; // INFO, WARNING, DANGER
}