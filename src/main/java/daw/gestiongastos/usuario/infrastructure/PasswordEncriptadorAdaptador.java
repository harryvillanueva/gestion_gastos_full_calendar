package daw.gestiongastos.usuario.infrastructure;

import daw.gestiongastos.usuario.domain.IPasswordEncriptador;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncriptadorAdaptador implements IPasswordEncriptador {

    // Inyectamos la herramienta de Spring
    private final PasswordEncoder passwordEncoder;

    public PasswordEncriptadorAdaptador(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encriptar(String passwordPlana) {
        // Usamos BCrypt para generar el hash indescifrable
        return passwordEncoder.encode(passwordPlana);
    }
}