package daw.gestiongastos.usuario.domain;

public interface IPasswordEncriptador {
    String encriptar(String passwordPlana);
    boolean coinciden(String passwordPlana, String passwordEncriptada);
}
