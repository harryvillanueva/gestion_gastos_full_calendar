package daw.gestiongastos.movimiento.application;

import daw.gestiongastos.movimiento.domain.IMovimientoRepositorio;
import daw.gestiongastos.movimiento.domain.Movimiento;
import daw.gestiongastos.notificacion.infraestructure.NotificacionApp;
import org.springframework.stereotype.Service;

@Service
public class EliminarMovimientoApp {
    private final IMovimientoRepositorio repositorio;
    private final NotificacionApp notificacionApp; // Inyectamos notificaciones

    public EliminarMovimientoApp(IMovimientoRepositorio repositorio, NotificacionApp notificacionApp) {
        this.repositorio = repositorio;
        this.notificacionApp = notificacionApp;
    }

    public void ejecutar(Long id, Long usuarioIdPeticion, String rol) {
        // 1. Buscamos el movimiento ANTES de borrarlo para saber de quién era
        Movimiento actual = repositorio.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));

        // 2. Validamos permisos
        if (!actual.getUsuarioId().equals(usuarioIdPeticion) && !"ADMIN".equals(rol)) {
            throw new RuntimeException("No tienes permiso para eliminar este movimiento");
        }

        // 3. Lo eliminamos de la base de datos
        repositorio.eliminar(id);

        // 4. 🚀 MAGIA: Si el ADMIN lo borró y no era suyo, le mandamos una notificación al dueño
        if ("ADMIN".equals(rol) && !actual.getUsuarioId().equals(usuarioIdPeticion)) {
            notificacionApp.crear(actual.getUsuarioId(),
                    "El Admin ha eliminado tu movimiento: " + actual.getDescripcion());
        }
    }
}