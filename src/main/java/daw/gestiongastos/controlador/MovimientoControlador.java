package daw.gestiongastos.controlador;


import daw.gestiongastos.aplicacion.IMovimientoAplicacionServicio;
import daw.gestiongastos.entidad.Movimiento;

import daw.gestiongastos.servicio.IStorageServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MovimientoControlador {


    @Autowired
    private IMovimientoAplicacionServicio movimientoAplicacionServicio;

    @Autowired
    private IStorageServicio storageServicio;



    @GetMapping("/")
    public String iniciar(ModelMap modelo) {

        movimientoAplicacionServicio.prepararDashboard(modelo);
        return "index";
    }

    @GetMapping("/agregar")
    public String mostrarAgregar(ModelMap modelo) {

        movimientoAplicacionServicio.prepararPaginaAgregar(modelo);
        return "agregar";
    }

    @PostMapping("/agregar")
    public String agregar(@ModelAttribute("movimientoForma") Movimiento movimiento,@RequestParam("file") MultipartFile file) {

        movimientoAplicacionServicio.guardarMovimiento(movimiento,file);
        return "redirect:/";
    }

    @GetMapping("/editar/{id}")
    public String mostrarEditar(@PathVariable(value = "id") int idMovimiento, ModelMap modelo) {

        movimientoAplicacionServicio.prepararPaginaEditar(idMovimiento, modelo);
        return "editar";
    }

    @PostMapping("/editar")
    public String editar(@ModelAttribute("movimiento") Movimiento movimiento,@RequestParam("file") MultipartFile file) {

        movimientoAplicacionServicio.guardarMovimiento(movimiento,file);
        return "redirect:/";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") int idMovimiento, RedirectAttributes redirectAttributes) {

        movimientoAplicacionServicio.eliminarMovimiento(idMovimiento);
        redirectAttributes.addFlashAttribute("msg_exito", "¡Se eliminó correctamente!");
        return "redirect:/";
    }

    @GetMapping("/comprobantes/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> servirFichero(@PathVariable String filename, HttpServletRequest request) {

        Resource file = storageServicio.loadAsResource(filename);


        String contentType = null;
        try {

            contentType = request.getServletContext().getMimeType(file.getFile().getAbsolutePath());
        } catch (Exception ex) {

        }


        if (contentType == null) {
            contentType = "application/octet-stream";
        }


        return ResponseEntity.ok()

                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(file);
    }
}