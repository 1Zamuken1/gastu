package gastu.gastu.infrastructure.adapter.output.persistence.mapper;

import gastu.gastu.domain.model.income.Ingreso;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.ConceptoIngresoEntity;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.IngresoEntity;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.UsuarioEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre Ingreso (Domain) e IngresoEntity (Persistence)
 */
@Component
public class IngresoPersistenceMapper {

    /**
     * Convierte de IngresoEntity (JPA) a Ingreso (Domain)
     */
    public Ingreso toDomain(IngresoEntity entity) {
        if (entity == null) {
            return null;
        }

        return Ingreso.builder()
                .ingresoId(entity.getIngresoId())
                .monto(entity.getMonto())
                .descripcion(entity.getDescripcion())
                .fechaRegistro(entity.getFechaRegistro())
                .fechaTransaccion(entity.getFechaTransaccion())
                .activo(entity.isActivo())
                .conceptoIngresoId(entity.getConceptoIngreso() != null
                        ? entity.getConceptoIngreso().getConceptoIngresoId()
                        : null)
                .usuarioId(entity.getUsuario() != null
                        ? entity.getUsuario().getUsuarioId()
                        : null)
                .build();
    }

    /**
     * Convierte de Ingreso (Domain) a IngresoEntity (JPA)
     */
    public IngresoEntity toEntity(Ingreso domain) {
        if (domain == null) {
            return null;
        }

        IngresoEntity entity = IngresoEntity.builder()
                .ingresoId(domain.getIngresoId())
                .monto(domain.getMonto())
                .descripcion(domain.getDescripcion())
                .fechaRegistro(domain.getFechaRegistro())
                .fechaTransaccion(domain.getFechaTransaccion())
                .activo(domain.isActivo())
                .build();

        // Setear relaciones si existen los IDs
        if (domain.getConceptoIngresoId() != null) {
            ConceptoIngresoEntity concepto = new ConceptoIngresoEntity();
            concepto.setConceptoIngresoId(domain.getConceptoIngresoId());
            entity.setConceptoIngreso(concepto);
        }

        if (domain.getUsuarioId() != null) {
            UsuarioEntity usuario = new UsuarioEntity();
            usuario.setUsuarioId(domain.getUsuarioId());
            entity.setUsuario(usuario);
        }

        return entity;
    }
}