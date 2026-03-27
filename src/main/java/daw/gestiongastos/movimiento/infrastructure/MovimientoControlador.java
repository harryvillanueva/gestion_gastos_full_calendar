package daw.gestiongastos.movimiento.infrastructure;

import daw.gestiongastos.movimiento.application.*;
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
    private final EditarMovimientoApp editarMovimientoApp;
    private final EliminarMovimientoApp eliminarMovimientoApp;
    private final TransferirFondosApp transferirFondosApp;

    public MovimientoControlador(CrearMovimientoApp crearMovimientoApp, ListarMovimientosApp listarMovimientosApp,
                                 EditarMovimientoApp editarMovimientoApp, EliminarMovimientoApp eliminarMovimientoApp, TransferirFondosApp transferirFondosApp) {
        this.crearMovimientoApp = crearMovimientoApp;
        this.listarMovimientosApp = listarMovimientosApp;
        this.editarMovimientoApp = editarMovimientoApp;
        this.eliminarMovimientoApp = eliminarMovimientoApp;
        this.transferirFondosApp = transferirFondosApp;
    }

    @PostMapping
    public ResponseEntity<?> crear(
            @RequestBody CrearMovimientoDTO dto,
            @RequestAttribute("usuarioId") Long usuarioId,
            @RequestAttribute("rol") String rol) {
        try {
            Long idFinal = ("ADMIN".equals(rol) && dto.getUsuarioIdDestino() != null) ? dto.getUsuarioIdDestino() : usuarioId;

            Movimiento guardado = crearMovimientoApp.ejecutar(
                    dto.getDescripcion(), dto.getImporte(), dto.getTipo(), dto.getFecha(), idFinal
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listar(
            @RequestAttribute("usuarioId") Long usuarioId,
            @RequestAttribute("rol") String rol) {
        return ResponseEntity.ok(listarMovimientosApp.ejecutar(usuarioId, rol));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(
            @PathVariable Long id,
            @RequestBody CrearMovimientoDTO dto,
            @RequestAttribute("usuarioId") Long usuarioId,
            @RequestAttribute("rol") String rol) {
        try {
            Movimiento editado = editarMovimientoApp.ejecutar(id, dto.getDescripcion(), dto.getImporte(), dto.getTipo(), usuarioId, rol);
            return ResponseEntity.ok(editado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
            @PathVariable Long id,
            @RequestAttribute("usuarioId") Long usuarioIdPeticion,
            @RequestAttribute("rol") String rol) {
        try {
            eliminarMovimientoApp.ejecutar(id, usuarioIdPeticion, rol);
            return ResponseEntity.ok("Movimiento eliminado correctamente");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error al eliminar");
        }
    }

    @PostMapping("/transferir")
    public ResponseEntity<?> transferir(@RequestBody TransferenciaDTO dto, @RequestAttribute("usuarioId") Long usuarioId) {
        try {
            transferirFondosApp.ejecutar(usuarioId, dto.getUsuarioIdDestino(), dto.getImporte());
            return ResponseEntity.ok("Transferencia realizada");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}