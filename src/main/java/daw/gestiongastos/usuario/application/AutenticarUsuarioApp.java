package daw.gestiongastos.usuario.application;

import daw.gestiongastos.usuario.domain.IPasswordEncriptador;
import daw.gestiongastos.usuario.domain.ITokenGenerador;
import daw.gestiongastos.usuario.domain.IUsuarioRepositorio;
import daw.gestiongastos.usuario.domain.Usuario;
import org.springframework.stereotype.Service;

@Service
public class AutenticarUsuarioApp {

    private final IUsuarioRepositorio usuarioRepositorio;
    private final IPasswordEncriptador encriptador;
    private final ITokenGenerador tokenGenerador;

    public AutenticarUsuarioApp(IUsuarioRepositorio usuarioRepositorio, IPasswordEncriptador encriptador, ITokenGenerador tokenGenerador) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.encriptador = encriptador;
        this.tokenGenerador = tokenGenerador;
    }

    public String ejecutar(String username, String password) {
        Usuario usuario = usuarioRepositorio.buscarPorUsername(username)
                .orElseThrow(() -> new RuntimeException("Credenciales incorrectas"));

        if (!encriptador.coinciden(password, usuario.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        return tokenGenerador.generarToken(usuario);
    }
}