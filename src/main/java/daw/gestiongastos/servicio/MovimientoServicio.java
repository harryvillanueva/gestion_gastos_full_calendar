package daw.gestiongastos.servicio;

import daw.gestiongastos.entidad.Movimiento;
import daw.gestiongastos.repositorio.MovimientoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovimientoServicio implements IMovimientoServicio{

    @Autowired
    private MovimientoRepositorio movimientoRepositorio;

    @Override
    public List<Movimiento> listarMovimientos() {
        return movimientoRepositorio.findAll();
    }

    @Override
    public Movimiento buscarMovimientoPorId(Integer idMovimiento) {

        Movimiento movimiento = movimientoRepositorio.findById(idMovimiento).orElse(null);

        return movimiento;
    }

    @Override
    public void agregarMovimiento(Movimiento movimiento) {

        movimientoRepositorio.save(movimiento);

    }

    @Override
    public void eliminarMovimiento(Movimiento movimiento) {

        movimientoRepositorio.delete(movimiento);

    }

    @Override
    public Double getTotalIngresos() {
        return movimientoRepositorio.findTotalIngresos();
    }

    @Override
    public Double getTotalGastos() {
        return movimientoRepositorio.findTotalGastos();
    }
}
