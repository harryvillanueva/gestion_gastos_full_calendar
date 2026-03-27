package daw.gestiongastos.usuario.infrastructure;

import daw.gestiongastos.usuario.domain.IUsuarioRepositorio;
import daw.gestiongastos.usuario.domain.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UsuarioRepositorioAdaptador implements IUsuarioRepositorio {

    private final UsuarioRepository jpaRepository;

    public UsuarioRepositorioAdaptador(UsuarioRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Usuario guardar(Usuario usuario) {

        UsuarioEntity entity = new UsuarioEntity(
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.getEmail(),
                usuario.getNombre(),
                usuario.getApellidos(),
                usuario.getRol()
        );


        UsuarioEntity entityGuardado = jpaRepository.save(entity);


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

    @Override
    public List<Usuario> buscarTodos() {
        return jpaRepository.findAll().stream()
                .map(entity -> new Usuario(
                        entity.getId(), entity.getNombre(), entity.getApellidos(),
                        entity.getEmail(), entity.getUsername(), entity.getPassword(), entity.getRol()))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return jpaRepository.findById(id).map(entity -> new Usuario(
                entity.getId(), entity.getNombre(), entity.getApellidos(),
                entity.getEmail(), entity.getUsername(), entity.getPassword(), entity.getRol()));
    }
}