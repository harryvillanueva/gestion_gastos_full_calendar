package daw.gestiongastos.aplicacion;

import daw.gestiongastos.entidad.Categoria;
import daw.gestiongastos.servicio.ICategoriaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Service
public class CategoriaAplicacionServicio implements ICategoriaAplicacionServicio {


    @Autowired
    private ICategoriaServicio categoriaServicio;

    @Override
    @Transactional(readOnly = true)
    public void prepararPaginaIndex(ModelMap modelo) {
        List<Categoria> categorias = categoriaServicio.listarCategorias();
        modelo.put("categorias", categorias);
    }

    @Override
    @Transactional(readOnly = true)
    public Categoria obtenerCategoriaParaEditar(int idCategoria) {
        return categoriaServicio.buscarCategoriaPorId(idCategoria);
    }

    @Override
    @Transactional
    public void guardarCategoria(Categoria categoria, RedirectAttributes redirectAttributes) {
        categoriaServicio.guardarCategoria(categoria);

        redirectAttributes.addFlashAttribute("msg_exito", "¡Categoría guardada correctamente!");
    }

    @Override
    @Transactional
    public void eliminarCategoria(int idCategoria, RedirectAttributes redirectAttributes) {

        try {
            Categoria categoria = new Categoria(idCategoria);
            categoriaServicio.eliminarCategoria(categoria);
            redirectAttributes.addFlashAttribute("msg_exito", "¡Categoría eliminada correctamente!");
        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("msg_error", "No se puede eliminar la categoría, está siendo usada por un movimiento.");
        }
    }
}