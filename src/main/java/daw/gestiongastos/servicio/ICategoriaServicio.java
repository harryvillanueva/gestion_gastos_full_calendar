package daw.gestiongastos.servicio;

import daw.gestiongastos.entidad.Categoria;
import java.util.List;

public interface ICategoriaServicio {
    public List<Categoria> listarCategorias();
    public Categoria buscarCategoriaPorId(Integer idCategoria);
    public Categoria guardarCategoria(Categoria categoria);
    public void eliminarCategoria(Categoria categoria);
}