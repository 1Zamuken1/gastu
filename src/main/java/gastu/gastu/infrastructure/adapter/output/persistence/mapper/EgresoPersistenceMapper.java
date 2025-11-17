package gastu.gastu.infrastructure.adapter.output.persistence.mapper;

import gastu.gastu.domain.model.expense.Egreso;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.ConceptoEgresoEntity;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.EgresoEntity;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.UsuarioEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre Egreso (Domain) y EgresoEntity (Persistence)
 */
@Component
public class EgresoPersistenceMapper {

    /**
     * Convierte de EgresoEntity (JPA) a Egreso (Domain)
     */
    public Egreso toDomain(EgresoEntity entity) {
        if (entity == null) {
            return null;
        }

        return Egreso.builder()
                .egresoId(entity.getEgresoId())
                .monto(entity.getMonto())
                .descripcion(entity.getDescripcion())
                .fechaRegistro(entity.getFechaRegistro())
                .fechaTransaccion(entity.getFechaTransaccion())
                .activo(entity.isActivo())
                .conceptoEgresoId(entity.getConceptoEgreso() != null
                        ? entity.getConceptoEgreso().getConceptoEgresoId()
                        : null)
                .usuarioId(entity.getUsuario() != null
                        ? entity.getUsuario().getUsuarioId()
                        : null)
                .build();
    }

    /**
     * Convierte de Egreso (Domain) a EgresoEntity (JPA)
     */
    public EgresoEntity toEntity(Egreso domain) {
        if (domain == null) {
            return null;
        }

        EgresoEntity entity = EgresoEntity.builder()
                .egresoId(domain.getEgresoId())
                .monto(domain.getMonto())
                .descripcion(domain.getDescripcion())
                .fechaRegistro(domain.getFechaRegistro())
                .fechaTransaccion(domain.getFechaTransaccion())
                .activo(domain.isActivo())
                .build();

        // Setear relaciones si existen los IDs
        if (domain.getConceptoEgresoId() != null) {
            ConceptoEgresoEntity concepto = new ConceptoEgresoEntity();
            concepto.setConceptoEgresoId(domain.getConceptoEgresoId());
            entity.setConceptoEgreso(concepto);
        }

        if (domain.getUsuarioId() != null) {
            UsuarioEntity usuario = new UsuarioEntity();
            usuario.setUsuarioId(domain.getUsuarioId());
            entity.setUsuario(usuario);
        }

        return entity;
    }
}