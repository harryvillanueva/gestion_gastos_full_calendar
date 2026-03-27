package daw.gestiongastos.movimiento.application;

import daw.gestiongastos.movimiento.domain.IMovimientoRepositorio;
import daw.gestiongastos.movimiento.domain.Movimiento;
import daw.gestiongastos.movimiento.domain.TipoMovimiento;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class CrearMovimientoApp {

    private final IMovimientoRepositorio repositorio;

    public CrearMovimientoApp(IMovimientoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public Movimiento ejecutar(String descripcion, BigDecimal importe, TipoMovimiento tipo, LocalDate fecha, Long usuarioId) {
        Movimiento nuevoMovimiento = new Movimiento(descripcion, importe, tipo, fecha, usuarioId);

        return repositorio.guardar(nuevoMovimiento);
    }
}