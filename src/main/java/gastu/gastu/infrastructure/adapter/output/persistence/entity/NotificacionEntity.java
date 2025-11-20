package gastu.gastu.infrastructure.adapter.output.persistence.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Entidad JPA que mapea la tabla 'notificacion'
 */
@Entity
@Table(name = "notificacion")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notificacion_id")
    private Long notificacionId;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo;

    @Column(name = "categoria", length = 30)
    private String categoria;

    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;

    @Column(name = "mensaje", nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    @Column(name = "datos", columnDefinition = "LONGTEXT")
    @Transient // Lo manejamos manualmente
    private Map<String, Object> datos;

    @Column(name = "datos", columnDefinition = "LONGTEXT")
    private String datosJson; // Campo real en BD

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "leida", nullable = false)
    private boolean leida = false;

    @Column(name = "fecha_lectura")
    private LocalDateTime fechaLectura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", insertable = false, updatable = false)
    private UsuarioEntity usuario;

    // Conversor JSON manual
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @PostLoad
    private void deserializeDatos() {
        if (datosJson != null && !datosJson.isEmpty()) {
            try {
                this.datos = objectMapper.readValue(datosJson, Map.class);
            } catch (JsonProcessingException e) {
                this.datos = new HashMap<>();
            }
        }
    }

    @PrePersist
    @PreUpdate
    private void serializeDatos() {
        if (datos != null) {
            try {
                this.datosJson = objectMapper.writeValueAsString(datos);
            } catch (JsonProcessingException e) {
                this.datosJson = "{}";
            }
        }
    }
}