package gastu.gastu.infrastructure.adapter.output.persistence.repository;

import gastu.gastu.infrastructure.adapter.output.persistence.entity.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad Rol
 */
@Repository
public interface JpaRolRepository extends JpaRepository<RolEntity, Integer> {

    /**
     * Busca un rol por su nombre
     */
    Optional<RolEntity> findByNombre(String nombre);
}