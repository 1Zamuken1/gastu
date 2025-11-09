package gastu.gastu.infrastructure.adapter.output.persistence.mapper;

import gastu.gastu.domain.model.user.Rol;
import gastu.gastu.domain.model.user.Usuario;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.RolEntity;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.UsuarioEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre Usuario (Domain) y UsuarioEntity (Persistence)
 */
@Component
public class UsuarioPersistenceMapper {

    /**
     * Convierte de UsuarioEntity (JPA) a Usuario (Domain)
     */
    public Usuario toDomain(UsuarioEntity entity) {
        if (entity == null) {
            return null;
        }

        return Usuario.builder()
                .usuarioId(entity.getUsuarioId())
                .nombre(entity.getNombre())
                .correo(entity.getCorreo())
                .telefono(entity.getTelefono())
                .password(entity.getPassword())
                .fechaRegistro(entity.getFechaRegistro())
                .fechaActualizacion(entity.getFechaActualizacion())
                .activo(entity.isActivo())
                .rol(toRolDomain(entity.getRol()))
                .build();
    }

    /**
     * Convierte de Usuario (Domain) a UsuarioEntity (JPA)
     */
    public UsuarioEntity toEntity(Usuario domain) {
        if (domain == null) {
            return null;
        }

        return UsuarioEntity.builder()
                .usuarioId(domain.getUsuarioId())
                .nombre(domain.getNombre())
                .correo(domain.getCorreo())
                .telefono(domain.getTelefono())
                .password(domain.getPassword())
                .fechaRegistro(domain.getFechaRegistro())
                .fechaActualizacion(domain.getFechaActualizacion())
                .activo(domain.isActivo())
                .rol(toRolEntity(domain.getRol()))
                .build();
    }

    /**
     * Convierte RolEntity a Rol (Domain)
     */
    private Rol toRolDomain(RolEntity entity) {
        if (entity == null) {
            return null;
        }

        return Rol.builder()
                .rolId(entity.getRolId())
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .fechaCreacion(entity.getFechaCreacion())
                .activo(entity.isActivo())
                .build();
    }

    /**
     * Convierte Rol (Domain) a RolEntity
     */
    private RolEntity toRolEntity(Rol domain) {
        if (domain == null) {
            return null;
        }

        return RolEntity.builder()
                .rolId(domain.getRolId())
                .nombre(domain.getNombre())
                .descripcion(domain.getDescripcion())
                .fechaCreacion(domain.getFechaCreacion())
                .activo(domain.isActivo())
                .build();
    }
}