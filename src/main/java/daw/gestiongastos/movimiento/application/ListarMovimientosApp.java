package daw.gestiongastos.movimiento.application;

import daw.gestiongastos.movimiento.domain.IMovimientoRepositorio;
import daw.gestiongastos.movimiento.domain.Movimiento;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ListarMovimientosApp {
    private final IMovimientoRepositorio repositorio;

    public ListarMovimientosApp(IMovimientoRepositorio repositorio) { this.repositorio = repositorio; }

    public List<Movimiento> ejecutar(Long usuarioId, String rol) {
        if ("ADMIN".equals(rol)) {
            return repositorio.buscarTodos();
        }
        return repositorio.buscarPorUsuarioId(usuarioId);
    }

    public List<Movimiento> ejecutar(Long usuarioId) {
        return repositorio.buscarPorUsuarioId(usuarioId);
    }
}