package daw.gestiongastos.movimiento.infrastructure;

import daw.gestiongastos.movimiento.domain.IMovimientoRepositorio;
import daw.gestiongastos.movimiento.domain.Movimiento;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MovimientoRepositorioAdaptador implements IMovimientoRepositorio {

    private final MovimientoJpaRepository jpaRepository;

    public MovimientoRepositorioAdaptador(MovimientoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Movimiento guardar(Movimiento movimiento) {
        MovimientoEntity entity = new MovimientoEntity(
                movimiento.getDescripcion(), movimiento.getImporte(),
                movimiento.getTipo(), movimiento.getFecha(), movimiento.getUsuarioId()
        );
        if (movimiento.getId() != null) { entity.setId(movimiento.getId()); } // Por si estamos editando

        MovimientoEntity guardado = jpaRepository.save(entity);

        return mapearADominio(guardado);
    }

    @Override
    public List<Movimiento> buscarPorUsuarioId(Long usuarioId) {
        return jpaRepository.findByUsuarioId(usuarioId).stream()
                .map(this::mapearADominio)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Movimiento> buscarPorId(Long id) {
        return jpaRepository.findById(id).map(this::mapearADominio);
    }

    @Override
    public void eliminar(Long id) {
        jpaRepository.deleteById(id);
    }

    // Método auxiliar para no repetir código
    private Movimiento mapearADominio(MovimientoEntity entity) {
        return new Movimiento(entity.getId(), entity.getDescripcion(), entity.getImporte(),
                entity.getTipo(), entity.getFecha(), entity.getUsuarioId());
    }
}