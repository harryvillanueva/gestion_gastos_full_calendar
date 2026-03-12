package daw.gestiongastos.entidad;

import jakarta.persistence.*; // Importante: Asegúrate de importar @ManyToOne, @JoinColumn
import java.sql.Date;

@Entity
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer idMovimiento;
    double monto;
    String movimientoTipo;
    Date fechaMovimiento;
    String descripcion;
    @ManyToOne
    @JoinColumn(name = "id_categoria")
            Categoria categoria;
    private String comprobante;


    public Movimiento(Integer idMovimiento) {
        this.idMovimiento = idMovimiento;
    }


    public Movimiento(Integer idMovimiento, double monto, String movimientoTipo, Date fechaMovimiento, String descripcion, Categoria categoria) {
        this.idMovimiento = idMovimiento;
        this.monto = monto;
        this.movimientoTipo = movimientoTipo;
        this.fechaMovimiento = fechaMovimiento;
        this.descripcion = descripcion;
        this.categoria = categoria;
    }

    public Movimiento() {
    }


    @Override
    public String toString() {
        return "Movimiento{" +
                "idMovimiento=" + idMovimiento +
                ", monto=" + monto +
                ", movimientoTipo='" + movimientoTipo + '\'' +
                ", fechaMovimiento=" + fechaMovimiento +
                ", descripcion='" + descripcion + '\'' +
                ", categoria=" + (categoria != null ? categoria.getNombre() : "null") + // Mostramos el nombre
                '}';
    }


    public Integer getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(Integer idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getMovimientoTipo() {
        return movimientoTipo;
    }

    public void setMovimientoTipo(String movimientoTipo) {
        this.movimientoTipo = movimientoTipo;
    }

    public Date getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(Date fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getComprobante() {
        return comprobante;
    }

    public void setComprobante(String comprobante) {
        this.comprobante = comprobante;
    }
}