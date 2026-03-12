package daw.gestiongastos.repositorio;

import daw.gestiongastos.entidad.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimientoRepositorio extends JpaRepository<Movimiento,Integer> {


    @Query("SELECT COALESCE(SUM(m.monto), 0.0) FROM Movimiento m WHERE m.movimientoTipo = 'Ingreso'")
    Double findTotalIngresos();

    @Query("SELECT COALESCE(SUM(m.monto), 0.0) FROM Movimiento m WHERE m.movimientoTipo = 'Gasto'")
    Double findTotalGastos();

}