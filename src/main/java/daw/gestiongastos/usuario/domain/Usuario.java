package daw.gestiongastos.usuario.domain;

public class Usuario {
    private Long id;
    private String username;
    private String password; // Estará encriptada
    private Rol rol;

    // Constructor para registrar un nuevo usuario (sin ID)
    public Usuario(String username, String password, Rol rol) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }
        this.username = username;
        this.password = password;
        this.rol = rol != null ? rol : Rol.BASICO; // Por defecto es básico
    }

    // Constructor para recuperar de la base de datos (con ID)
    public Usuario(Long id, String username, String password, Rol rol) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    // Getters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Rol getRol() { return rol; }
}