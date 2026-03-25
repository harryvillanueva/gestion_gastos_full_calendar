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
        // La clase Movimiento (Dominio) ya valida que el importe sea > 0 y la descripción no esté vacía
        Movimiento nuevoMovimiento = new Movimiento(descripcion, importe, tipo, fecha, usuarioId);

        // Lo guardamos a través del puerto
        return repositorio.guardar(nuevoMovimiento);
    }
}