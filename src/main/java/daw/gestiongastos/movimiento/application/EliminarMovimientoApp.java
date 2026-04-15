package daw.gestiongastos.movimiento.application;

import daw.gestiongastos.movimiento.domain.IMovimientoRepositorio;
import daw.gestiongastos.movimiento.domain.Movimiento;
import daw.gestiongastos.notificacion.application.NotificacionApp;
import org.springframework.stereotype.Service;

@Service
public class EliminarMovimientoApp {
    private final IMovimientoRepositorio repositorio;
    private final NotificacionApp notificacionApp;

    public EliminarMovimientoApp(IMovimientoRepositorio repositorio, NotificacionApp notificacionApp) {
        this.repositorio = repositorio;
        this.notificacionApp = notificacionApp;
    }

    public void ejecutar(Long id, Long usuarioIdPeticion, String rol) {
        Movimiento actual = repositorio.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));

        if (!actual.getUsuarioId().equals(usuarioIdPeticion) && !"ADMIN".equals(rol)) {
            throw new RuntimeException("No tienes permiso para eliminar este movimiento");
        }

        repositorio.eliminar(id);

        if ("ADMIN".equals(rol) && !actual.getUsuarioId().equals(usuarioIdPeticion)) {
            notificacionApp.crear(actual.getUsuarioId(),
                    "El Admin ha eliminado tu movimiento: " + actual.getDescripcion());
        }
    }
}