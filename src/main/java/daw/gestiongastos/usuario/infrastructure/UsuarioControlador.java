package daw.gestiongastos.usuario.infrastructure.controlador;

import daw.gestiongastos.usuario.application.RegistrarUsuarioApp;
import daw.gestiongastos.usuario.domain.Usuario;
import daw.gestiongastos.usuario.infrastructure.RegistroUsuarioDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*") // Permite que tu futuro frontend se conecte sin bloqueos
public class UsuarioControlador {

    private final RegistrarUsuarioApp registrarUsuarioApp;

    // Inyectamos nuestro Caso de Uso
    public UsuarioControlador(RegistrarUsuarioApp registrarUsuarioApp) {
        this.registrarUsuarioApp = registrarUsuarioApp;
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@RequestBody RegistroUsuarioDTO datosEntrada) {
        try {
            // 1. Extraemos TODOS los datos del DTO y se los pasamos al Director de Orquesta (Caso de Uso)
            Usuario nuevoUsuario = registrarUsuarioApp.ejecutar(
                    datosEntrada.getUsername(),
                    datosEntrada.getPassword(),
                    datosEntrada.getEmail(),      // Nuevo campo
                    datosEntrada.getNombre(),     // Nuevo campo
                    datosEntrada.getApellidos(),  // Nuevo campo
                    datosEntrada.getRol()
            );

            // 2. Si todo va bien (código 201 Created), devolvemos el usuario creado en formato JSON
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);

        } catch (RuntimeException e) {
            // 3. Si salta alguna regla de negocio (ej. email sin '@' o usuario duplicado)
            // devolvemos un código 400 Bad Request con el mensaje de error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}