package gastu.gastu.domain.model.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Entidad de dominio que representa una Notificación
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notificacion {

    private Long notificacionId;
    private Long usuarioId;
    private TipoNotificacion tipo;
    private CategoriaNotificacion categoria;
    private String titulo;
    private String mensaje;
    private Map<String, Object> datos; // JSON flexible
    private LocalDateTime fechaCreacion;
    private boolean leida;
    private LocalDateTime fechaLectura;

    /**
     * Marca la notificación como leída
     */
    public void marcarComoLeida() {
        this.leida = true;
        this.fechaLectura = LocalDateTime.now();
    }

    /**
     * Verifica si la notificación es reciente (menos de 24 horas)
     */
    public boolean isReciente() {
        if (fechaCreacion == null) return false;
        return fechaCreacion.isAfter(LocalDateTime.now().minusHours(24));
    }

    /**
     * Enums
     */
    public enum TipoNotificacion {
        ALERTA_SALDO,
        CONFIRMACION_PROYECCION,
        MENSAJE_IA,
        META_PROXIMA
    }

    public enum CategoriaNotificacion {
        FINANCIERA,
        SISTEMA,
        IA
    }
}