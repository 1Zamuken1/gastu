package gastu.gastu.infrastructure.adapter.output.persistence.adapter;

import gastu.gastu.application.port.output.EgresoRepositoryPort;
import gastu.gastu.domain.model.expense.Egreso;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.EgresoEntity;
import gastu.gastu.infrastructure.adapter.output.persistence.repository.JpaEgresoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EgresoRepositoryAdapter implements EgresoRepositoryPort {

    private final JpaEgresoRepository jpaEgresoRepository;

    @Override
    public Egreso save(Egreso egreso) {
        // Mapear de dominio a entidad
        EgresoEntity entity = EgresoEntity.builder()
                .monto(egreso.getMonto())
                .descripcion(egreso.getDescripcion())
                .fechaTransaccion(egreso.getFechaTransaccion())
                .activo(egreso.isActivo())
                .conceptoEgreso(null) // Aquí mapearías ConceptoEgreso a ConceptoEgresoEntity
                .usuario(null)         // Aquí mapearías Usuario a UsuarioEntity
                .build();

        EgresoEntity saved = jpaEgresoRepository.save(entity);

        // Mapear de entidad a dominio
        egreso.setEgresoId(saved.getEgresoId());
        return egreso;
    }

    @Override
    public Optional<Egreso> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Egreso> findActivosByUsuario(Long usuarioId) {
        return List.of();
    }

    @Override
    public List<Egreso> findAllByUsuario(Long usuarioId) {
        return List.of();
    }

    @Override
    public List<Egreso> findByUsuarioAndFechaRange(Long usuarioId, LocalDate fechaInicio, LocalDate fechaFin) {
        return List.of();
    }

    @Override
    public List<Egreso> findByUsuarioAndMes(Long usuarioId, int mes, int anio) {
        return List.of();
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public long countActivosByUsuario(Long usuarioId) {
        return 0;
    }

    @Override
    public List<Egreso> findByUsuarioId(Long usuarioId) {
        List<EgresoEntity> entities = jpaEgresoRepository.findByUsuarioUsuarioIdAndActivoTrue(usuarioId);
        // Mapear lista de entidades a lista de dominio
        return entities.stream().map(e -> Egreso.builder()
                .egresoId(e.getEgresoId())
                .monto(e.getMonto())
                .descripcion(e.getDescripcion())
                .fechaTransaccion(e.getFechaTransaccion())
                .activo(e.isActivo())
                .build()
        ).toList();
    }
}
