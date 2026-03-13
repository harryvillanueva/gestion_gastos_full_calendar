package daw.gestiongastos.usuario.infrastructure;

import daw.gestiongastos.usuario.domain.IUsuarioRepositorio;
import daw.gestiongastos.usuario.domain.Usuario;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsuarioRepositorioAdaptador implements IUsuarioRepositorio {

    private final UsuarioRepository jpaRepository;

    public UsuarioRepositorioAdaptador(UsuarioRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        // 1. Traducimos del Dominio a la Entidad de BD (ahora con más campos)
        UsuarioEntity entity = new UsuarioEntity(
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.getEmail(),
                usuario.getNombre(),
                usuario.getApellidos(),
                usuario.getRol()
        );

        // 2. Guardamos en la BD usando Spring
        UsuarioEntity entityGuardado = jpaRepository.save(entity);

        // 3. Traducimos la respuesta de la BD de vuelta a nuestro Dominio
        return new Usuario(
                entityGuardado.getId(),
                entityGuardado.getUsername(),
                entityGuardado.getPassword(),
                entityGuardado.getEmail(),
                entityGuardado.getNombre(),
                entityGuardado.getApellidos(),
                entityGuardado.getRol()
        );
    }

    @Override
    public Optional<Usuario> buscarPorUsername(String username) {
        return jpaRepository.findByUsername(username)
                .map(entity -> new Usuario(
                        entity.getId(),
                        entity.getUsername(),
                        entity.getPassword(),
                        entity.getEmail(),
                        entity.getNombre(),
                        entity.getApellidos(),
                        entity.getRol()
                ));
    }

    @Override
    public boolean existeUsername(String username) {
        return jpaRepository.existsByUsername(username);
    }
}