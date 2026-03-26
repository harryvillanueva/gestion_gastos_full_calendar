package daw.gestiongastos.movimiento.application;

import daw.gestiongastos.movimiento.domain.IMovimientoRepositorio;
import daw.gestiongastos.movimiento.domain.Movimiento;
import daw.gestiongastos.movimiento.domain.TipoMovimiento;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class EditarMovimientoApp {
    private final IMovimientoRepositorio repositorio;

    public EditarMovimientoApp(IMovimientoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public Movimiento ejecutar(Long id, String nuevaDescripcion, BigDecimal nuevoImporte, TipoMovimiento nuevoTipo, Long usuarioIdPeticion) {
        // 1. Buscamos el movimiento original en la BD
        Movimiento actual = repositorio.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));

        // 2. Medida de seguridad: Comprobar que el usuario que edita es el dueño
        // (Más adelante añadiremos la excepción si es Admin)
        if (!actual.getUsuarioId().equals(usuarioIdPeticion)) {
            throw new RuntimeException("No tienes permiso para editar este movimiento");
        }

        // 3. Creamos la versión actualizada RESPETANDO LA FECHA ORIGINAL
        Movimiento actualizado = new Movimiento(
                actual.getId(),
                nuevaDescripcion,
                nuevoImporte,
                nuevoTipo,
                actual.getFecha(), // ¡La fecha no se toca!
                actual.getUsuarioId()
        );

        // 4. Guardamos
        return repositorio.guardar(actualizado);
    }
}