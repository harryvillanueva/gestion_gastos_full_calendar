package daw.gestiongastos.usuario.infrastructure;

import daw.gestiongastos.usuario.application.RegistrarUsuarioApp;
import daw.gestiongastos.usuario.domain.Rol;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/usuarios")
public class UsuarioControlador {
    // Inyectamos nuestro Caso de Uso. ¡Fíjate que no inyectamos repositorios aquí!
    private final RegistrarUsuarioApp registrarUsuarioApp;

    public UsuarioControlador(RegistrarUsuarioApp registrarUsuarioApp) {
        this.registrarUsuarioApp = registrarUsuarioApp;
    }

    // 1. Mostrar el formulario HTML al usuario
    @GetMapping("/registro")
    public String mostrarFormularioRegistro() {
        return "usuarios/registro"; // Esto buscará el archivo templates/usuarios/registro.html
    }

    // 2. Procesar los datos cuando el usuario envíe el formulario
    @PostMapping("/registro")
    public String registrarUsuario(
            @RequestParam String username,
            @RequestParam String password,
            Model modelo) {

        try {
            // Ejecutamos nuestro Caso de Uso. Por defecto, le asignamos el rol BASICO.
            registrarUsuarioApp.ejecutar(username, password, Rol.BASICO);

            // Si todo sale bien, lo mandamos al login o al index con un mensaje de éxito
            return "redirect:/?exito=Usuario+registrado+correctamente";

        } catch (RuntimeException e) {
            // Si nuestro Caso de Uso lanza un error (ej. el usuario ya existe o contraseña corta)
            // Atrapamos el error y volvemos a mostrar el formulario con el mensaje de error
            modelo.addAttribute("error", e.getMessage());
            modelo.addAttribute("username", username); // Para que no tenga que volver a escribir el nombre
            return "usuarios/registro";
        }
    }
}
