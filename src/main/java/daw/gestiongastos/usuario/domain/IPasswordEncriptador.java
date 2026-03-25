package daw.gestiongastos.usuario.domain;

public interface IPasswordEncriptador {
    String encriptar(String passwordPlana);

    // NUEVO MÉTODO: Para el Login
    boolean coinciden(String passwordPlana, String passwordEncriptada);
}
