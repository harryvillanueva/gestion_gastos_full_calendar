package daw.gestiongastos.usuario.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    // Con solo escribir este nombre, Spring Boot crea automáticamente la consulta SQL:
    // SELECT * FROM usuarios WHERE username = ?
    Optional<UsuarioEntity> findByUsername(String username);

    // Y este nos servirá para saber si un nombre ya está en uso al registrarse
    boolean existsByUsername(String username);
}