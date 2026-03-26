package daw.gestiongastos.movimiento.infrastructure;

import daw.gestiongastos.movimiento.application.CrearMovimientoApp;
import daw.gestiongastos.movimiento.application.EditarMovimientoApp; // NUEVO
import daw.gestiongastos.movimiento.application.EliminarMovimientoApp; // NUEVO
import daw.gestiongastos.movimiento.application.ListarMovimientosApp;
import daw.gestiongastos.movimiento.domain.Movimiento;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movimientos")
@CrossOrigin(origins = "*")
public class MovimientoControlador {

    private final CrearMovimientoApp crearMovimientoApp;
    private final ListarMovimientosApp listarMovimientosApp;
    private final EditarMovimientoApp editarMovimientoApp;     // NUEVO
    private final EliminarMovimientoApp eliminarMovimientoApp; // NUEVO

    // Inyectamos todos los Casos de Uso en el constructor
    public MovimientoControlador(CrearMovimientoApp crearMovimientoApp, ListarMovimientosApp listarMovimientosApp,
                                 EditarMovimientoApp editarMovimientoApp, EliminarMovimientoApp eliminarMovimientoApp) {
        this.crearMovimientoApp = crearMovimientoApp;
        this.listarMovimientosApp = listarMovimientosApp;
        this.editarMovimientoApp = editarMovimientoApp;
        this.eliminarMovimientoApp = eliminarMovimientoApp;
    }

    // ... (Tus métodos @PostMapping y @GetMapping siguen igual aquí) ...

    // NUEVO ENDPOINT PARA EDITAR
    @PutMapping("/{id}")
    public ResponseEntity<?> editar(
            @PathVariable Long id,
            @RequestBody CrearMovimientoDTO dto, // Reciclamos el DTO porque tiene los mismos campos
            @RequestAttribute("usuarioId") Long usuarioId) {
        try {
            Movimiento editado = editarMovimientoApp.ejecutar(id, dto.getDescripcion(), dto.getImporte(), dto.getTipo(), usuarioId);
            return ResponseEntity.ok(editado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // NUEVO ENDPOINT PARA BORRAR
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            eliminarMovimientoApp.ejecutar(id);
            return ResponseEntity.ok("Movimiento eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar");
        }
    }
}