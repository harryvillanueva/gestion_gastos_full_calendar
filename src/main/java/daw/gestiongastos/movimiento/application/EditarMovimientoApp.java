package daw.gestiongastos.movimiento.application;

import daw.gestiongastos.movimiento.domain.IMovimientoRepositorio;
import daw.gestiongastos.movimiento.domain.Movimiento;
import daw.gestiongastos.movimiento.domain.TipoMovimiento;
import daw.gestiongastos.notificacion.infraestructure.NotificacionApp;
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
        // 1. Buscamos el movimiento original
        Movimiento actual = repositorio.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));

        // 2. Validamos permisos: Si NO es el dueño Y TAMPOCO es Admin, bloqueamos.
        if (!actual.getUsuarioId().equals(usuarioIdPeticion) && !"ADMIN".equals(rol)) {
            throw new RuntimeException("No tienes permiso para editar este movimiento");
        }

        // 3. Creamos la versión actualizada (respetando la fecha original y el usuario original)
        Movimiento actualizado = new Movimiento(
                actual.getId(),
                nuevaDescripcion,
                nuevoImporte,
                nuevoTipo,
                actual.getFecha(),
                actual.getUsuarioId()
        );

        // 4. Guardamos los cambios en la BD
        Movimiento guardado = repositorio.guardar(actualizado);

        // 5. 🚀 MAGIA: Si el que editó fue el ADMIN, y el movimiento no era suyo, avisamos al dueño original.
        if ("ADMIN".equals(rol) && !actual.getUsuarioId().equals(usuarioIdPeticion)) {
            notificacionApp.crear(actual.getUsuarioId(),
                    "El Admin ha editado tu movimiento. Nueva descripción: " + nuevaDescripcion);
        }

        return guardado;
    }
}