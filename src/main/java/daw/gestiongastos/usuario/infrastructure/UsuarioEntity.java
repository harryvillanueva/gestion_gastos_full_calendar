package daw.gestiongastos.usuario.infrastructure;

import daw.gestiongastos.usuario.domain.Rol;
import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellidos;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    public UsuarioEntity() {
    }

    public UsuarioEntity(String username, String password, String email, String nombre, String apellidos, Rol rol) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.rol = rol;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
}