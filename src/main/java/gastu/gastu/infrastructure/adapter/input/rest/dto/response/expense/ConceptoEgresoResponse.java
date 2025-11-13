package gastu.gastu.infrastructure.adapter.input.rest.dto.response.expense;

import com.fasterxml.jackson.annotation.JsonFormat;
import gastu.gastu.domain.model.shared.TipoTransaccion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para Concepto de Egreso
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConceptoEgresoResponse {

    private Long conceptoEgresoId;
    private String nombre;
    private String descripcion;
    private TipoTransaccion tipoTransaccion;
    private boolean activo;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaCreacion;
}