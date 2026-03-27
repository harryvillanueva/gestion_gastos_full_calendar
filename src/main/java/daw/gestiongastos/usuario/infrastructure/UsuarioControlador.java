package daw.gestiongastos.usuario.infrastructure;

import daw.gestiongastos.usuario.application.AutenticarUsuarioApp;
import daw.gestiongastos.usuario.application.RegistrarUsuarioApp;
import daw.gestiongastos.usuario.domain.IUsuarioRepositorio;
import daw.gestiongastos.usuario.infrastructure.dto.LoginUsuarioDTO;
import daw.gestiongastos.usuario.infrastructure.dto.RegistroUsuarioDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioControlador {

    private final RegistrarUsuarioApp registrarUsuarioApp;
    private final AutenticarUsuarioApp autenticarUsuarioApp;
    private final IUsuarioRepositorio usuarioRepositorio; // Inyectamos el repo

    public UsuarioControlador(RegistrarUsuarioApp registrarUsuarioApp, AutenticarUsuarioApp autenticarUsuarioApp, IUsuarioRepositorio usuarioRepositorio) {
        this.registrarUsuarioApp = registrarUsuarioApp;
        this.autenticarUsuarioApp = autenticarUsuarioApp;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody RegistroUsuarioDTO dto) {
        try {
            registrarUsuarioApp.ejecutar(dto.getNombre(), dto.getApellidos(), dto.getEmail(), dto.getUsername(), dto.getPassword(), dto.getRol());
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado con éxito");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUsuarioDTO datosLogin) {
        try {
            String token = autenticarUsuarioApp.ejecutar(datosLogin.getUsername(), datosLogin.getPassword());
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("token", token);
            return ResponseEntity.ok(respuesta);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    // 🚀 NUEVO ENDPOINT: Exclusivo para que el ADMIN vea a quién asignarle gastos
    @GetMapping("/todos")
    public ResponseEntity<?> obtenerTodosLosUsuarios(@RequestAttribute("rol") String rol) {
        if (!"ADMIN".equals(rol)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permisos para ver esto");
        }
        return ResponseEntity.ok(usuarioRepositorio.buscarTodos());
    }

    @GetMapping("/todos")
    public ResponseEntity<?> obtenerTodosLosUsuarios() {
        // Ahora todos pueden ver la lista para poder hacer transferencias
        return ResponseEntity.ok(usuarioRepositorio.buscarTodos());
    }
}