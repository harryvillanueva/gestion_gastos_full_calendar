package daw.gestiongastos.movimiento.infrastructure;

import java.math.BigDecimal;

public class TransferenciaDTO {
    private Long usuarioIdDestino;
    private BigDecimal importe;

    public Long getUsuarioIdDestino() { return usuarioIdDestino; }
    public void setUsuarioIdDestino(Long usuarioIdDestino) { this.usuarioIdDestino = usuarioIdDestino; }
    public BigDecimal getImporte() { return importe; }
    public void setImporte(BigDecimal importe) { this.importe = importe; }
}