package daw.gestiongastos.controlador;


import daw.gestiongastos.aplicacion.ICategoriaAplicacionServicio;
import daw.gestiongastos.entidad.Categoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CategoriaControlador {


    @Autowired
    private ICategoriaAplicacionServicio categoriaAplicacionServicio;

    @GetMapping("/categorias")
    public String listarCategorias(ModelMap modelo) {
        categoriaAplicacionServicio.prepararPaginaIndex(modelo);
        return "categorias/index";
    }


    @GetMapping("/categorias/agregar")
    public String mostrarAgregarCategoria() {

        return "categorias/agregar";
    }


    @PostMapping("/categorias/agregar")
    public String agregarCategoria(@ModelAttribute("categoriaForma") Categoria categoria, RedirectAttributes redirectAttributes) {
        categoriaAplicacionServicio.guardarCategoria(categoria, redirectAttributes);
        return "redirect:/categorias";
    }


    @GetMapping("/categorias/editar/{id}")
    public String mostrarEditarCategoria(@PathVariable(value = "id") int idCategoria, ModelMap modelo) {
        Categoria categoria = categoriaAplicacionServicio.obtenerCategoriaParaEditar(idCategoria);


        if (categoria == null) {
            return "redirect:/categorias";
        }
        modelo.put("categoria", categoria);
        return "categorias/editar";
    }


    @PostMapping("/categorias/editar")
    public String editarCategoria(@ModelAttribute("categoria") Categoria categoria, RedirectAttributes redirectAttributes) {
        categoriaAplicacionServicio.guardarCategoria(categoria, redirectAttributes);
        return "redirect:/categorias";
    }


    @GetMapping("/categorias/eliminar/{id}")
    public String eliminarCategoria(@PathVariable(value = "id") int idCategoria, RedirectAttributes redirectAttributes) {

        categoriaAplicacionServicio.eliminarCategoria(idCategoria, redirectAttributes);
        return "redirect:/categorias";
    }
}