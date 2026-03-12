package daw.gestiongastos.servicio;

import daw.gestiongastos.entidad.Movimiento;

import java.util.List;

public interface IMovimientoServicio {



    public List<Movimiento> listarMovimientos();
    public Movimiento buscarMovimientoPorId(Integer idMovimiento);
    public void agregarMovimiento(Movimiento movimiento);
    public void eliminarMovimiento(Movimiento movimiento);

    public Double getTotalIngresos();
    public Double getTotalGastos();


}
