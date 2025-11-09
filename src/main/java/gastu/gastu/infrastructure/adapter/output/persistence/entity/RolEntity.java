package gastu.gastu.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Entidad JPA que mapea la tabla 'rol' en la base de datos
 */
@Entity
@Table(name = "rol")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rol_id")
    private Integer rolId;

    @Column(name = "nombre", nullable = false, unique = true, length = 30)
    private String nombre;

    @Column(name = "descripcion", length = 100)
    private String descripcion;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "activo", nullable = false)
    private boolean activo = true;
}