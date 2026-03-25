package daw.gestiongastos.movimiento.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MovimientoJpaRepository extends JpaRepository<MovimientoEntity, Long> {
    // Magia de Spring: Busca todos los movimientos de un usuario específico
    List<MovimientoEntity> findByUsuarioId(Long usuarioId);
}