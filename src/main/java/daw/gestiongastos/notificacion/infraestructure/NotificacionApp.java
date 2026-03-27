package daw.gestiongastos.notificacion.infraestructure;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin(origins = "*")
@Service
public class NotificacionApp {

    private final NotificacionJpaRepository repo;

    public NotificacionApp(NotificacionJpaRepository repo) { this.repo = repo; }

    public void crear(Long usuarioId, String mensaje) {
        repo.save(new NotificacionEntity(usuarioId, mensaje));
    }

    @GetMapping
    public ResponseEntity<List<NotificacionEntity>> listar(@RequestAttribute("usuarioId") Long usuarioId) {
        return ResponseEntity.ok(repo.findByUsuarioIdOrderByFechaDesc(usuarioId));
    }
}