package gastu.gastu.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entidad JPA que mapea la tabla 'usuario' en la base de datos
 */
@Entity
@Table(name = "usuario")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "nombre", nullable = false, length = 30)
    private String nombre;

    @Column(name = "correo", nullable = false, unique = true, length = 50)
    private String correo;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @CreationTimestamp
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "activo", nullable = false)
    private boolean activo = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id", nullable = false, foreignKey = @ForeignKey(name = "fk_usuario_rol"))
    private RolEntity rol;
}