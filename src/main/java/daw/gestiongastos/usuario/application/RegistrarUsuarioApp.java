package daw.gestiongastos.usuario.application;

import daw.gestiongastos.usuario.domain.IUsuarioRepositorio;
import daw.gestiongastos.usuario.domain.Rol;
import daw.gestiongastos.usuario.domain.Usuario;
import org.springframework.stereotype.Service;

@Service
public class RegistrarUsuarioApp {

    private final IUsuarioRepositorio repositorio;

    public RegistrarUsuarioApp(IUsuarioRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    // Actualizamos la firma del método para recibir los nuevos campos
    public Usuario ejecutar(String username, String password, String email, String nombre, String apellidos, Rol rol) {

        // 1. Regla de negocio: Verificar si el usuario ya existe
        if (repositorio.existeUsername(username)) {
            throw new RuntimeException("El nombre de usuario '" + username + "' ya está registrado.");
        }

        // 2. Crear el objeto de dominio con los nuevos campos
        Usuario nuevoUsuario = new Usuario(username, password, email, nombre, apellidos, rol);

        // 3. Guardar y retornar
        return repositorio.guardar(nuevoUsuario);
    }
}