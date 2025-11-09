package gastu.gastu.infrastructure.adapter.output.persistence.repository;

import gastu.gastu.infrastructure.adapter.output.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad Usuario
 */
@Repository
public interface JpaUsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    /**
     * Busca un usuario por su correo electrónico
     */
    Optional<UsuarioEntity> findByCorreo(String correo);

    /**
     * Verifica si existe un usuario con el correo especificado
     */
    boolean existsByCorreo(String correo);

    /**
     * Verifica si existe un usuario con el teléfono especificado
     */
    boolean existsByTelefono(String telefono);
}