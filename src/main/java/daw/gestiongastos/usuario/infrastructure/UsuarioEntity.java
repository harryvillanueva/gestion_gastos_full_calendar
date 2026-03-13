package daw.gestiongastos.usuario.infrastructure;

import daw.gestiongastos.usuario.domain.Rol;
import jakarta.persistence.*;

@Entity
@Table(name = "usuarios") // Así se llamará la tabla en MySQL
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Esto hace que el ID sea Autoincremental
    private Long id;

    // unique = true asegura que en la base de datos no haya dos usuarios con el mismo nombre
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    // Guardamos el Enum como un texto (String) en la base de datos (ej: "ADMIN", "BASICO")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    // JPA OBLIGA a tener un constructor vacío, aunque no lo usemos nosotros directamente
    public UsuarioEntity() {
    }

    // Constructor para mapear desde nuestro objeto de Dominio a esta Entidad
    public UsuarioEntity(String username, String password, Rol rol) {
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
}