package daw.gestiongastos.movimiento.infrastructure;

import daw.gestiongastos.movimiento.application.CrearMovimientoApp;
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

    public MovimientoControlador(CrearMovimientoApp crearMovimientoApp, ListarMovimientosApp listarMovimientosApp) {
        this.crearMovimientoApp = crearMovimientoApp;
        this.listarMovimientosApp = listarMovimientosApp;
    }

    @GetMapping
    public ResponseEntity<?> listar(@RequestAttribute("usuarioId") Long usuarioId) {
        return ResponseEntity.ok(listarMovimientosApp.ejecutar(usuarioId));
    }

    @PostMapping
    public ResponseEntity<?> crear(
            @RequestBody CrearMovimientoDTO dto,
            // ¡FÍJATE AQUÍ! Sacamos el ID que nuestro JwtFiltro dejó guardado en la petición
            @RequestAttribute("usuarioId") Long usuarioId) {

        try {
            Movimiento guardado = crearMovimientoApp.ejecutar(
                    dto.getDescripcion(),
                    dto.getImporte(),
                    dto.getTipo(),
                    dto.getFecha(),
                    usuarioId // ¡Le pasamos el ID seguro y comprobado!
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}