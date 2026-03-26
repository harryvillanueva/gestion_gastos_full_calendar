package daw.gestiongastos.movimiento.application;

import daw.gestiongastos.movimiento.domain.IMovimientoRepositorio;
import org.springframework.stereotype.Service;

@Service
public class EliminarMovimientoApp {
    private final IMovimientoRepositorio repositorio;

    public EliminarMovimientoApp(IMovimientoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public void ejecutar(Long id) {
        // Aquí podríamos añadir reglas, como verificar si existe antes de borrar
        repositorio.eliminar(id);
    }
}