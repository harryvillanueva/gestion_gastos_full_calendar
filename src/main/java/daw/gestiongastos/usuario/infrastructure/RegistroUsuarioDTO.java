package daw.gestiongastos.usuario.infrastructure;

import daw.gestiongastos.usuario.domain.Rol;

public class RegistroUsuarioDTO {

    private String username;
    private String password;
    private String email;     // Añadido
    private String nombre;    // Añadido
    private String apellidos; // Añadido
    private Rol rol;

    public RegistroUsuarioDTO() {}

    // Getters y Setters
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