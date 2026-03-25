package daw.gestiongastos.usuario.domain;

public interface ITokenGenerador {
    String generarToken(Usuario usuario);
}
