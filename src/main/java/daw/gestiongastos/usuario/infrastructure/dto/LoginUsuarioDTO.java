package daw.gestiongastos.usuario.infrastructure.dto; // O .dto si creaste la carpeta

public class LoginUsuarioDTO {
    private String username;
    private String password;

    public LoginUsuarioDTO() {}

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}