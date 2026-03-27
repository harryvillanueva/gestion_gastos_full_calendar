package daw.gestiongastos.movimiento.domain;

import java.util.List;
import java.util.Optional;

public interface IMovimientoRepositorio {
    Movimiento guardar(Movimiento movimiento);
    List<Movimiento> buscarPorUsuarioId(Long usuarioId);
    Optional<Movimiento> buscarPorId(Long id);
    void eliminar(Long id);
    List<Movimiento> buscarTodos();
}