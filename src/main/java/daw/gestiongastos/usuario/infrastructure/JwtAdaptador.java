package daw.gestiongastos.usuario.infrastructure;

import daw.gestiongastos.usuario.domain.ITokenGenerador;
import daw.gestiongastos.usuario.domain.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtAdaptador implements ITokenGenerador {

    // Generamos una clave secreta segura en memoria para firmar los tokens
    private static final Key CLAVE_SECRETA = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long TIEMPO_EXPIRACION = 86400000; // 1 día en milisegundos

    @Override
    public String generarToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getUsername())
                .claim("rol", usuario.getRol().name()) // Metemos el rol dentro del token
                .claim("id", usuario.getId())          // Metemos el ID para futuras consultas
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TIEMPO_EXPIRACION))
                .signWith(CLAVE_SECRETA)
                .compact();
    }
}