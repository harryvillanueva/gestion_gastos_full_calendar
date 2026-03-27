package daw.gestiongastos.notificacion.infraestructure;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificacionJpaRepository extends JpaRepository<NotificacionEntity, Long> {
    List<NotificacionEntity> findByUsuarioIdOrderByFechaDesc(Long usuarioId);
}
