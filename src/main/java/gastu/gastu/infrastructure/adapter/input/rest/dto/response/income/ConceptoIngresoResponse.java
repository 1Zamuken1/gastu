package gastu.gastu.infrastructure.adapter.input.rest.dto.response.income;

import com.fasterxml.jackson.annotation.JsonFormat;
import gastu.gastu.domain.model.shared.TipoTransaccion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para Concepto de Ingreso
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConceptoIngresoResponse {

    private Long conceptoIngresoId;
    private String nombre;
    private String descripcion;
    private TipoTransaccion tipoTransaccion;
    private boolean activo;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaCreacion;
}