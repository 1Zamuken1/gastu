package gastu.gastu.domain.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad de dominio que representa un Rol del sistema.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rol {

    private Integer rolId;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private boolean activo;

    /**
     * Roles predefinidos del sistema
     */
    public static final String ADMINISTRADOR = "ADMINISTRADOR";
    public static final String INSTRUCTOR = "INSTRUCTOR";
    public static final String APRENDIZ = "APRENDIZ";

    /**
     * Verifica si es un rol válido del sistema
     */
    public boolean isRolValido() {
        return ADMINISTRADOR.equals(nombre)
                || INSTRUCTOR.equals(nombre)
                || APRENDIZ.equals(nombre);
    }
}