package gastu.gastu.infrastructure.adapter.output.persistence.adapter;

import gastu.gastu.application.port.output.IngresoRepositoryPort;
import gastu.gastu.domain.model.income.Ingreso;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.IngresoEntity;
import gastu.gastu.infrastructure.adapter.output.persistence.mapper.IngresoPersistenceMapper;
import gastu.gastu.infrastructure.adapter.output.persistence.repository.JpaIngresoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador que implementa el puerto IngresoRepositoryPort
 * Conecta el dominio con la capa de persistencia JPA
 */
@Component
@RequiredArgsConstructor
public class IngresoRepositoryAdapter implements IngresoRepositoryPort {

    private final JpaIngresoRepository jpaRepository;
    private final IngresoPersistenceMapper mapper;

    @Override
    public Ingreso save(Ingreso ingreso) {
        IngresoEntity entity = mapper.toEntity(ingreso);
        IngresoEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Ingreso> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Ingreso> findByUsuarioIdAndActivoTrue(Long usuarioId) {
        return jpaRepository.findByUsuarioUsuarioIdAndActivoTrue(usuarioId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Ingreso> findByUsuarioId(Long usuarioId) {
        return jpaRepository.findByUsuarioUsuarioId(usuarioId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Ingreso> findByUsuarioIdAndFechaTransaccionBetween(
            Long usuarioId,
            LocalDate fechaInicio,
            LocalDate fechaFin) {
        return jpaRepository.findByUsuarioAndFechaTransaccionBetween(usuarioId, fechaInicio, fechaFin)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Ingreso> findByConceptoIngresoId(Long conceptoIngresoId) {
        return jpaRepository.findByConceptoIngresoConceptoIngresoId(conceptoIngresoId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void desactivarByConceptoId(Long conceptoIngresoId) {
        jpaRepository.desactivarByConceptoId(conceptoIngresoId);
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