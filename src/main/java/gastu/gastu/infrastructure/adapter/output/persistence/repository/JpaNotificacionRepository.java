package gastu.gastu.infrastructure.adapter.output.persistence.repository;

import gastu.gastu.infrastructure.adapter.output.persistence.entity.NotificacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para Notificacion
 */
@Repository
public interface JpaNotificacionRepository extends JpaRepository<NotificacionEntity, Long> {

    /**
     * Busca notificaciones no leídas de un usuario
     */
    List<NotificacionEntity> findByUsuarioIdAndLeidaFalseOrderByFechaCreacionDesc(Long usuarioId);

    /**
     * Busca todas las notificaciones de un usuario
     */
    List<NotificacionEntity> findByUsuarioIdOrderByFechaCreacionDesc(Long usuarioId);

    /**
     * Cuenta notificaciones no leídas de un usuario
     */
    long countByUsuarioIdAndLeidaFalse(Long usuarioId);
}