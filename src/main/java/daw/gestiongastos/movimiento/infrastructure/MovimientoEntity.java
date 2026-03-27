package daw.gestiongastos.movimiento.infrastructure;

import daw.gestiongastos.movimiento.domain.TipoMovimiento;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "movimientos")
public class MovimientoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal importe;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimiento tipo;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    public MovimientoEntity() {}

    public MovimientoEntity(String descripcion, BigDecimal importe, TipoMovimiento tipo, LocalDate fecha, Long usuarioId) {
        this.descripcion = descripcion;
        this.importe = importe;
        this.tipo = tipo;
        this.fecha = fecha;
        this.usuarioId = usuarioId;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public BigDecimal getImporte() { return importe; }
    public void setImporte(BigDecimal importe) { this.importe = importe; }
    public TipoMovimiento getTipo() { return tipo; }
    public void setTipo(TipoMovimiento tipo) { this.tipo = tipo; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
}