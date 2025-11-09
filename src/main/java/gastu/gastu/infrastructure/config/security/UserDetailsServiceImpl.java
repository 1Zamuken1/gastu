package gastu.gastu.infrastructure.config.security;

import gastu.gastu.infrastructure.adapter.output.persistence.entity.UsuarioEntity;
import gastu.gastu.infrastructure.adapter.output.persistence.repository.JpaUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * Implementación de UserDetailsService para cargar usuarios desde la BD
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final JpaUsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        UsuarioEntity usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + correo));

        if (!usuario.isActivo()) {
            throw new UsernameNotFoundException("Usuario inactivo: " + correo);
        }

        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombre())
        );

        return User.builder()
                .username(usuario.getCorreo())
                .password(usuario.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!usuario.isActivo())
                .build();
    }
}