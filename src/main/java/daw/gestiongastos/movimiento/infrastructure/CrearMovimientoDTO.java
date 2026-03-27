package daw.gestiongastos.movimiento.infrastructure;

import daw.gestiongastos.movimiento.domain.TipoMovimiento;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CrearMovimientoDTO {
    private String descripcion;
    private BigDecimal importe;
    private TipoMovimiento tipo;
    private LocalDate fecha;
    private Long usuarioIdDestino;

    public CrearMovimientoDTO() {}


    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public BigDecimal getImporte() { return importe; }
    public void setImporte(BigDecimal importe) { this.importe = importe; }
    public TipoMovimiento getTipo() { return tipo; }
    public void setTipo(TipoMovimiento tipo) { this.tipo = tipo; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public Long getUsuarioIdDestino() { return usuarioIdDestino; }
    public void setUsuarioIdDestino(Long usuarioIdDestino) { this.usuarioIdDestino = usuarioIdDestino; }

}