package daw.gestiongastos.aplicacion;

import daw.gestiongastos.entidad.Categoria;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface ICategoriaAplicacionServicio {


    void prepararPaginaIndex(ModelMap modelo);
    Categoria obtenerCategoriaParaEditar(int idCategoria);
    void guardarCategoria(Categoria categoria, RedirectAttributes redirectAttributes);
    void eliminarCategoria(int idCategoria, RedirectAttributes redirectAttributes);
}