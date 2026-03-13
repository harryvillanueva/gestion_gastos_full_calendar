package daw.gestiongastos.usuario.application;

import daw.gestiongastos.usuario.domain.IUsuarioRepositorio;
import daw.gestiongastos.usuario.domain.Rol;
import daw.gestiongastos.usuario.domain.Usuario;
import org.springframework.stereotype.Service;

@Service
public class RegistrarUsuarioApp {

    // Nos comunicamos a través de la interfaz (Puerto), ¡no dependemos de la BD real!
    private final IUsuarioRepositorio repositorio;

    public RegistrarUsuarioApp(IUsuarioRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    // Este es el método que ejecutará el controlador cuando alguien rellene el formulario
    public Usuario ejecutar(String username, String password, Rol rol) {

        // 1. Regla de negocio: Verificar si el usuario ya existe
        if (repositorio.existeUsername(username)) {
            throw new RuntimeException("El nombre de usuario '" + username + "' ya está registrado.");
        }

        // 2. Crear el objeto de dominio (Aquí saltarán tus validaciones de longitud, etc.)
        // NOTA: Más adelante, aquí encriptaremos la contraseña antes de crear el usuario.
        Usuario nuevoUsuario = new Usuario(username, password, rol);

        // 3. Guardar y retornar
        return repositorio.guardar(nuevoUsuario);
    }
}