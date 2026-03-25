package daw.gestiongastos.usuario.infrastructure;

import daw.gestiongastos.usuario.domain.IPasswordEncriptador;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncriptadorAdaptador implements IPasswordEncriptador {

    private final PasswordEncoder passwordEncoder;

    public PasswordEncriptadorAdaptador(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encriptar(String passwordPlana) {
        return passwordEncoder.encode(passwordPlana);
    }

    // NUEVO MÉTODO IMPLEMENTADO: Usa Spring Security para comparar
    @Override
    public boolean coinciden(String passwordPlana, String passwordEncriptada) {
        return passwordEncoder.matches(passwordPlana, passwordEncriptada);
    }
}