package gastu.gastu.infrastructure.adapter.output.persistence.adapter;

import gastu.gastu.application.port.output.EgresoRepositoryPort;
import gastu.gastu.domain.model.expense.Egreso;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.EgresoEntity;
import gastu.gastu.infrastructure.adapter.output.persistence.mapper.EgresoPersistenceMapper;
import gastu.gastu.infrastructure.adapter.output.persistence.repository.JpaEgresoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador que implementa el puerto EgresoRepositoryPort
 * Conecta el dominio con la capa de persistencia JPA
 */
@Component
@RequiredArgsConstructor
public class EgresoRepositoryAdapter implements EgresoRepositoryPort {

    private final JpaEgresoRepository jpaRepository;
    private final EgresoPersistenceMapper mapper;

    @Override
    public Egreso save(Egreso egreso) {
        EgresoEntity entity = mapper.toEntity(egreso);
        EgresoEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Egreso> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Egreso> findByUsuarioIdAndActivoTrue(Long usuarioId) {
        return jpaRepository.findByUsuarioUsuarioIdAndActivoTrue(usuarioId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Egreso> findByUsuarioId(Long usuarioId) {
        return jpaRepository.findByUsuarioUsuarioId(usuarioId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Egreso> findByUsuarioIdAndFechaTransaccionBetween(
            Long usuarioId,
            LocalDate fechaInicio,
            LocalDate fechaFin) {
        return jpaRepository.findByUsuarioAndFechaTransaccionBetween(usuarioId, fechaInicio, fechaFin)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Egreso> findByConceptoEgresoId(Long conceptoEgresoId) {
        return jpaRepository.findByConceptoEgresoConceptoEgresoId(conceptoEgresoId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void desactivarByConceptoId(Long conceptoEgresoId) {
        jpaRepository.desactivarByConceptoId(conceptoEgresoId);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }
}