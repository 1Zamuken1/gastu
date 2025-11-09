package gastu.gastu.application.port.output;

import gastu.gastu.domain.model.user.Usuario;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para el repositorio de Usuario
 * Define las operaciones de persistencia sin depender de la implementación
 */
public interface UsuarioRepositoryPort {

    /**
     * Guarda un usuario nuevo o actualiza uno existente
     */
    Usuario save(Usuario usuario);

    /**
     * Busca un usuario por su ID
     */
    Optional<Usuario> findById(Long id);

    /**
     * Busca un usuario por su correo electrónico
     */
    Optional<Usuario> findByCorreo(String correo);

    /**
     * Busca todos los usuarios activos
     */
    List<Usuario> findAllActive();

    /**
     * Busca todos los usuarios
     */
    List<Usuario> findAll();

    /**
     * Verifica si existe un usuario con el correo especificado
     */
    boolean existsByCorreo(String correo);

    /**
     * Verifica si existe un usuario con el teléfono especificado
     */
    boolean existsByTelefono(String telefono);

    /**
     * Elimina un usuario (puede ser soft delete)
     */
    void delete(Long id);

    /**
     * Busca usuarios por rol
     */
    List<Usuario> findByRol(String nombreRol);
}