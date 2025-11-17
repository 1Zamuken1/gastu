package gastu.gastu.infrastructure.adapter.output.persistence.mapper;

import gastu.gastu.domain.model.income.ConceptoIngreso;
import gastu.gastu.domain.model.income.Ingreso;
import gastu.gastu.domain.model.user.Usuario;
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
                .conceptoIngreso(toConceptoDomain(entity.getConceptoIngreso()))
                .usuario(toUsuarioDomain(entity.getUsuario()))
                .build();
    }

    /**
     * Convierte de Ingreso (Domain) a IngresoEntity (JPA)
     */
    public IngresoEntity toEntity(Ingreso domain) {
        if (domain == null) {
            return null;
        }

        return IngresoEntity.builder()
                .ingresoId(domain.getIngresoId())
                .monto(domain.getMonto())
                .descripcion(domain.getDescripcion())
                .fechaRegistro(domain.getFechaRegistro())
                .fechaTransaccion(domain.getFechaTransaccion())
                .activo(domain.isActivo())
                .conceptoIngreso(toConceptoEntity(domain.getConceptoIngreso()))
                .usuario(toUsuarioEntity(domain.getUsuario()))
                .build();
    }

    // ===== CONVERSORES DE CONCEPTO =====

    private ConceptoIngreso toConceptoDomain(ConceptoIngresoEntity entity) {
        if (entity == null) {
            return null;
        }

        return ConceptoIngreso.builder()
                .conceptoIngresoId(entity.getConceptoIngresoId())
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .tipoTransaccion(entity.getTipoTransaccion())
                .activo(entity.isActivo())
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }

    private ConceptoIngresoEntity toConceptoEntity(ConceptoIngreso domain) {
        if (domain == null) {
            return null;
        }

        return ConceptoIngresoEntity.builder()
                .conceptoIngresoId(domain.getConceptoIngresoId())
                .nombre(domain.getNombre())
                .descripcion(domain.getDescripcion())
                .tipoTransaccion(domain.getTipoTransaccion())
                .activo(domain.isActivo())
                .fechaCreacion(domain.getFechaCreacion())
                .build();
    }

    // ===== CONVERSORES DE USUARIO =====

    private Usuario toUsuarioDomain(UsuarioEntity entity) {
        if (entity == null) {
            return null;
        }

        return Usuario.builder()
                .usuarioId(entity.getUsuarioId())
                .nombre(entity.getNombre())
                .correo(entity.getCorreo())
                .build();
    }

    private UsuarioEntity toUsuarioEntity(Usuario domain) {
        if (domain == null) {
            return null;
        }

        UsuarioEntity entity = new UsuarioEntity();
        entity.setUsuarioId(domain.getUsuarioId());
        return entity;
    }
}