package gastu.gastu.infrastructure.adapter.output.persistence.entity;

import gastu.gastu.domain.model.shared.Frecuencia;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad JPA que mapea la tabla 'proyeccion_ingreso'
 */
@Entity
@Table(name = "proyeccion_ingreso")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProyeccionIngresoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proyeccion_ingreso_id")
    private Long proyeccionIngresoId;

    @Column(name = "monto_programado", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoProgramado;

    @Column(name = "descripcion", nullable = false, length = 100)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "frecuencia", nullable = false, length = 30)
    private Frecuencia frecuencia;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "activo", nullable = false)
    private boolean activo = true;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "concepto_ingreso_id", nullable = false, foreignKey = @ForeignKey(name = "fk_proyeccion_ingreso_concepto"))
    private ConceptoIngresoEntity conceptoIngreso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, foreignKey = @ForeignKey(name = "fk_proyeccion_ingreso_usuario"))
    private UsuarioEntity usuario;
}