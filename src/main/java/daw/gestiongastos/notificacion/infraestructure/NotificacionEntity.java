package daw.gestiongastos.notificacion.infraestructure;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
public class NotificacionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usuarioId; // A quién va dirigida
    private String mensaje;
    private LocalDateTime fecha;

    public NotificacionEntity() {}
    public NotificacionEntity(Long usuarioId, String mensaje) {
        this.usuarioId = usuarioId;
        this.mensaje = mensaje;
        this.fecha = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getUsuarioId() { return usuarioId; }
    public String getMensaje() { return mensaje; }
    public LocalDateTime getFecha() { return fecha; }
}
