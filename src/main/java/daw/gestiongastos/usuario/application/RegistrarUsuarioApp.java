package daw.gestiongastos.usuario.application;

import daw.gestiongastos.usuario.domain.IPasswordEncriptador;
import daw.gestiongastos.usuario.domain.IUsuarioRepositorio;
import daw.gestiongastos.usuario.domain.Rol;
import daw.gestiongastos.usuario.domain.Usuario;
import org.springframework.stereotype.Service;

@Service
public class RegistrarUsuarioApp {

    private final IUsuarioRepositorio repositorio;
    private final IPasswordEncriptador encriptador;

    public RegistrarUsuarioApp(IUsuarioRepositorio repositorio, IPasswordEncriptador encriptador) {
        this.repositorio = repositorio;
        this.encriptador = encriptador;
    }

    public Usuario ejecutar(String username, String password, String email, String nombre, String apellidos, Rol rol) {

        if (repositorio.existeUsername(username)) {
            throw new RuntimeException("El nombre de usuario '" + username + "' ya está registrado.");
        }

        String passwordEncriptada = encriptador.encriptar(password);

        Usuario nuevoUsuario = new Usuario(username, passwordEncriptada, email, nombre, apellidos, rol);

        return repositorio.guardar(nuevoUsuario);
    }
}