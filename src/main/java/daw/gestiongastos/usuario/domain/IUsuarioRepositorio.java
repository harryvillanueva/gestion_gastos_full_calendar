package daw.gestiongastos.usuario.domain;

import java.util.Optional;

// Este es nuestro "Puerto". Pertenece al dominio.
public interface IUsuarioRepositorio {
    Usuario guardar(Usuario usuario);
    Optional<Usuario> buscarPorUsername(String username);
    boolean existeUsername(String username);
}