package gastu.gastu.infrastructure.adapter.output.persistence.adapter;

import gastu.gastu.application.port.output.UsuarioRepositoryPort;
import gastu.gastu.domain.model.user.Usuario;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.UsuarioEntity;
import gastu.gastu.infrastructure.adapter.output.persistence.mapper.UsuarioPersistenceMapper;
import gastu.gastu.infrastructure.adapter.output.persistence.repository.JpaUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador que implementa el puerto UsuarioRepositoryPort
 * Conecta el dominio con la capa de persistencia JPA
 */
@Component
@RequiredArgsConstructor
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final JpaUsuarioRepository jpaRepository;
    private final UsuarioPersistenceMapper mapper;

    @Override
    public Usuario save(Usuario usuario) {
        UsuarioEntity entity = mapper.toEntity(usuario);
        UsuarioEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Usuario> findByCorreo(String correo) {
        return jpaRepository.findByCorreo(correo)
                .map(mapper::toDomain);
    }

    @Override
    public List<Usuario> findAllActive() {
        return jpaRepository.findAll().stream()
                .filter(UsuarioEntity::isActivo)
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Usuario> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByCorreo(String correo) {
        return jpaRepository.existsByCorreo(correo);
    }

    @Override
    public boolean existsByTelefono(String telefono) {
        return jpaRepository.existsByTelefono(telefono);
    }

    @Override
    public void delete(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<Usuario> findByRol(String nombreRol) {
        return jpaRepository.findAll().stream()
                .filter(u -> u.getRol().getNombre().equals(nombreRol))
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}