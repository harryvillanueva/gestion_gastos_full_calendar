package daw.gestiongastos.aplicacion;

import daw.gestiongastos.entidad.Categoria;
import daw.gestiongastos.entidad.Movimiento;
import daw.gestiongastos.servicio.ICategoriaServicio;
import daw.gestiongastos.servicio.IMovimientoServicio;
import daw.gestiongastos.servicio.EmailServicio;
import jakarta.annotation.PostConstruct;
import daw.gestiongastos.servicio.IStorageServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class MovimientoAplicacionServicio implements IMovimientoAplicacionServicio {

    @Autowired
    private IMovimientoServicio movimientoServicio;

    @Autowired
    private ICategoriaServicio categoriaServicio;


    @Autowired
    private EmailServicio emailServicio;

    @Autowired
    private IStorageServicio storageServicio;


    private boolean alertaBalanceBajoEnviada = false;


    @PostConstruct
    public void init() {

        Double balance = movimientoServicio.getTotalIngresos() - movimientoServicio.getTotalGastos();

        this.alertaBalanceBajoEnviada = (balance <= 0);
        System.out.println("Estado inicial de alertaBalanceBajoEnviada: " + this.alertaBalanceBajoEnviada);
    }

    @Override
    @Transactional(readOnly = true)
    public void prepararDashboard(ModelMap modelo) {
        List<Movimiento> movimientos = movimientoServicio.listarMovimientos();
        Double totalIngresos = movimientoServicio.getTotalIngresos();
        Double totalGastos = movimientoServicio.getTotalGastos();
        Double balance = totalIngresos - totalGastos;

        modelo.put("movimientos", movimientos);
        modelo.put("totalIngresos", totalIngresos);
        modelo.put("totalGastos", totalGastos);
        modelo.put("balance", balance);
    }

    @Override
    @Transactional(readOnly = true)
    public void prepararPaginaAgregar(ModelMap modelo) {
        List<Categoria> categorias = categoriaServicio.listarCategorias();
        modelo.put("categorias", categorias);
    }

    @Override
    @Transactional
    public void guardarMovimiento(Movimiento movimiento, MultipartFile comprobante) {

        String comprobanteAntiguo = null;


        if (movimiento.getIdMovimiento() != null) {

            Movimiento movAntiguo = movimientoServicio.buscarMovimientoPorId(movimiento.getIdMovimiento());
            if (movAntiguo != null) {

                comprobanteAntiguo = movAntiguo.getComprobante();
            }
        }


        if (comprobante != null && !comprobante.isEmpty()) {

            String nuevoComprobante = storageServicio.store(comprobante);


            movimiento.setComprobante(nuevoComprobante);

            if (comprobanteAntiguo != null) {
                storageServicio.delete(comprobanteAntiguo);
            }
        } else {
            movimiento.setComprobante(comprobanteAntiguo);
        }



        movimientoServicio.agregarMovimiento(movimiento);


        verificarBalanceYEnviarAlerta();
    }

    @Override
    @Transactional(readOnly = true)
    public Movimiento prepararPaginaEditar(int idMovimiento, ModelMap modelo) {
        Movimiento movimiento = movimientoServicio.buscarMovimientoPorId(idMovimiento);
        if (movimiento != null) {
            modelo.put("movimiento", movimiento);
            List<Categoria> categorias = categoriaServicio.listarCategorias();
            modelo.put("categorias", categorias);
        }
        return movimiento;
    }

    @Override
    @Transactional
    public void eliminarMovimiento(int idMovimiento) {

        Movimiento movimiento = new Movimiento();
        movimiento.setIdMovimiento(idMovimiento);
        movimientoServicio.eliminarMovimiento(movimiento);


        verificarBalanceYEnviarAlerta();
    }


    private void verificarBalanceYEnviarAlerta() {

        Double balance = movimientoServicio.getTotalIngresos() - movimientoServicio.getTotalGastos();


        if (balance < 0 && !this.alertaBalanceBajoEnviada) {
            System.out.println("Balance bajo detectado. Enviando correo...");
            emailServicio.enviarNotificacionBalanceBajo(balance);
            this.alertaBalanceBajoEnviada = true;


        } else if (balance > 0 && this.alertaBalanceBajoEnviada) {
            System.out.println("Balance positivo restaurado. Reseteando bandera de alerta.");

            this.alertaBalanceBajoEnviada = false;
        }
    }
}