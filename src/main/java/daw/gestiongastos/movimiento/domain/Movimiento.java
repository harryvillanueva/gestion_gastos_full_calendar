package daw.gestiongastos.movimiento.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Movimiento {
    private Long id;
    private String descripcion;
    private BigDecimal importe;
    private TipoMovimiento tipo;
    private LocalDate fecha;
    private Long usuarioId;

    public Movimiento(String descripcion, BigDecimal importe, TipoMovimiento tipo, LocalDate fecha, Long usuarioId) {
        if (importe == null || importe.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El importe debe ser mayor a cero.");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción es obligatoria.");
        }
        if (usuarioId == null) {
            throw new IllegalArgumentException("El movimiento debe pertenecer a un usuario.");
        }

        this.descripcion = descripcion;
        this.importe = importe;
        this.tipo = tipo;
        this.fecha = fecha != null ? fecha : LocalDate.now();
        this.usuarioId = usuarioId;
    }


    public Movimiento(Long id, String descripcion, BigDecimal importe, TipoMovimiento tipo, LocalDate fecha, Long usuarioId) {
        this.id = id;
        this.descripcion = descripcion;
        this.importe = importe;
        this.tipo = tipo;
        this.fecha = fecha;
        this.usuarioId = usuarioId;
    }

    public Long getId() { return id; }
    public String getDescripcion() { return descripcion; }
    public BigDecimal getImporte() { return importe; }
    public TipoMovimiento getTipo() { return tipo; }
    public LocalDate getFecha() { return fecha; }
    public Long getUsuarioId() { return usuarioId; }
}