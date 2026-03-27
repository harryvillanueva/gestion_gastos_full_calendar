package daw.gestiongastos.movimiento.application;

import daw.gestiongastos.movimiento.domain.*;
import daw.gestiongastos.usuario.domain.IUsuarioRepositorio;
import daw.gestiongastos.usuario.domain.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class TransferirFondosApp {
    private final IMovimientoRepositorio movRepo;
    private final IUsuarioRepositorio usuRepo;

    public TransferirFondosApp(IMovimientoRepositorio movRepo, IUsuarioRepositorio usuRepo) {
        this.movRepo = movRepo;
        this.usuRepo = usuRepo;
    }

    @Transactional // ¡Asegura que se guarden los dos o ninguno!
    public void ejecutar(Long idOrigen, Long idDestino, BigDecimal importe) {
        if (importe.compareTo(BigDecimal.ZERO) <= 0) throw new RuntimeException("Importe inválido");

        Usuario origen = usuRepo.buscarPorId(idOrigen).orElseThrow();
        Usuario destino = usuRepo.buscarPorId(idDestino).orElseThrow();

        Movimiento gasto = new Movimiento("Transferencia a " + destino.getUsername(), importe, TipoMovimiento.GASTO, LocalDate.now(), idOrigen);
        Movimiento ingreso = new Movimiento("Transferencia de " + origen.getUsername(), importe, TipoMovimiento.INGRESO, LocalDate.now(), idDestino);

        movRepo.guardar(gasto);
        movRepo.guardar(ingreso);
    }
}