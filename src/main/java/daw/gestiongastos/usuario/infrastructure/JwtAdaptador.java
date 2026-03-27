package daw.gestiongastos.usuario.infrastructure;

import daw.gestiongastos.usuario.domain.ITokenGenerador;
import daw.gestiongastos.usuario.domain.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtAdaptador implements ITokenGenerador {


    private static final String CLAVE_SECRETA_TEXTO = "EstaEsUnaClaveSecretaMuyLargaYSuperSeguraParaNuestroProyectoDeGestionDeGastos12345";
    private static final Key CLAVE_SECRETA = Keys.hmacShaKeyFor(CLAVE_SECRETA_TEXTO.getBytes());

    private static final long TIEMPO_EXPIRACION = 86400000;

    @Override
    public String generarToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getUsername())
                .claim("rol", usuario.getRol().name())
                .claim("id", usuario.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TIEMPO_EXPIRACION))
                .signWith(CLAVE_SECRETA)
                .compact();
    }

    public Claims obtenerClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(CLAVE_SECRETA)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}