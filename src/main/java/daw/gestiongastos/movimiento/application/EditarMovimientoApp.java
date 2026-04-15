package daw.gestiongastos.movimiento.application;

import daw.gestiongastos.movimiento.domain.IMovimientoRepositorio;
import daw.gestiongastos.movimiento.domain.Movimiento;
import daw.gestiongastos.movimiento.domain.TipoMovimiento;
import daw.gestiongastos.notificacion.application.NotificacionApp;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class EditarMovimientoApp {
    private final IMovimientoRepositorio repositorio;
    private final NotificacionApp notificacionApp; // Inyectamos el módulo de notificaciones

    public EditarMovimientoApp(IMovimientoRepositorio repositorio, NotificacionApp notificacionApp) {
        this.repositorio = repositorio;
        this.notificacionApp = notificacionApp;
    }

    public Movimiento ejecutar(Long id, String nuevaDescripcion, BigDecimal nuevoImporte, TipoMovimiento nuevoTipo, Long usuarioIdPeticion, String rol) {
        Movimiento actual = repositorio.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));

        if (!actual.getUsuarioId().equals(usuarioIdPeticion) && !"ADMIN".equals(rol)) {
            throw new RuntimeException("No tienes permiso para editar este movimiento");
        }

        Movimiento actualizado = new Movimiento(
                actual.getId(),
                nuevaDescripcion,
                nuevoImporte,
                nuevoTipo,
                actual.getFecha(),
                actual.getUsuarioId()
        );

        Movimiento guardado = repositorio.guardar(actualizado);

        if ("ADMIN".equals(rol) && !actual.getUsuarioId().equals(usuarioIdPeticion)) {
            notificacionApp.crear(actual.getUsuarioId(),
                    "El Admin ha editado tu movimiento. Nueva descripción: " + nuevaDescripcion);
        }

        return guardado;
    }
}