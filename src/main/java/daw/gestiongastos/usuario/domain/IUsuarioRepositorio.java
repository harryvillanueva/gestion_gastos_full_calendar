package daw.gestiongastos.usuario.domain;

import java.util.List;
import java.util.Optional;

public interface IUsuarioRepositorio {
    Usuario guardar(Usuario usuario);
    Optional<Usuario> buscarPorUsername(String username);
    boolean existeUsername(String username);
    List<Usuario> buscarTodos();
    Optional<Usuario> buscarPorId(Long id);
}