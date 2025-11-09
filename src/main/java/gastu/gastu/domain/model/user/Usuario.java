package gastu.gastu.domain.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad de dominio que representa un Usuario del sistema.
 * Esta clase NO tiene dependencias de frameworks (JPA, Spring, etc.)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    private Long usuarioId;
    private String nombre;
    private String correo;
    private String telefono;
    private String password;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaActualizacion;
    private boolean activo;
    private Rol rol;

    /**
     * Valida que el correo tenga un formato básico válido
     */
    public boolean isCorreoValido() {
        return correo != null && correo.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    /**
     * Valida que el teléfono tenga un formato básico válido (10 dígitos)
     */
    public boolean isTelefonoValido() {
        return telefono == null || telefono.matches("^[0-9]{10}$");
    }

    /**
     * Verifica si el usuario es administrador
     */
    public boolean isAdministrador() {
        return rol != null && "ADMINISTRADOR".equals(rol.getNombre());
    }

    /**
     * Verifica si el usuario es instructor
     */
    public boolean isInstructor() {
        return rol != null && "INSTRUCTOR".equals(rol.getNombre());
    }

    /**
     * Verifica si el usuario es aprendiz
     */
    public boolean isAprendiz() {
        return rol != null && "APRENDIZ".equals(rol.getNombre());
    }

    /**
     * Desactiva el usuario (soft delete)
     */
    public void desactivar() {
        this.activo = false;
    }

    /**
     * Activa el usuario
     */
    public void activar() {
        this.activo = true;
    }
}