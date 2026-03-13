package daw.gestiongastos.usuario.application;

import daw.gestiongastos.usuario.domain.IPasswordEncriptador;
import daw.gestiongastos.usuario.domain.IUsuarioRepositorio;
import daw.gestiongastos.usuario.domain.Rol;
import daw.gestiongastos.usuario.domain.Usuario;
import org.springframework.stereotype.Service;

@Service
public class RegistrarUsuarioApp {

    private final IUsuarioRepositorio repositorio;
    private final IPasswordEncriptador encriptador; // <-- Añadimos nuestra nueva interfaz

    // Actualizamos el constructor para inyectar el encriptador
    public RegistrarUsuarioApp(IUsuarioRepositorio repositorio, IPasswordEncriptador encriptador) {
        this.repositorio = repositorio;
        this.encriptador = encriptador;
    }

    public Usuario ejecutar(String username, String password, String email, String nombre, String apellidos, Rol rol) {

        // 1. Verificar si existe
        if (repositorio.existeUsername(username)) {
            throw new RuntimeException("El nombre de usuario '" + username + "' ya está registrado.");
        }

        // 2. ¡MAGIA AQUÍ! Encriptamos la contraseña en texto plano
        String passwordEncriptada = encriptador.encriptar(password);

        // 3. Crear el objeto de dominio pasando la contraseña YA encriptada
        Usuario nuevoUsuario = new Usuario(username, passwordEncriptada, email, nombre, apellidos, rol);

        // 4. Guardar y retornar
        return repositorio.guardar(nuevoUsuario);
    }
}