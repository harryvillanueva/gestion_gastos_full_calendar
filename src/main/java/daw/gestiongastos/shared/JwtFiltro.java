package daw.gestiongastos.shared;

import daw.gestiongastos.usuario.infrastructure.JwtAdaptador;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFiltro extends OncePerRequestFilter {

    private final JwtAdaptador jwtAdaptador;

    public JwtFiltro(JwtAdaptador jwtAdaptador) {
        this.jwtAdaptador = jwtAdaptador;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // 1. Buscamos la cabecera "Authorization"
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            try {
                // 2. Quitamos la palabra "Bearer " para quedarnos solo con el token
                String token = header.substring(7);

                // 3. Extraemos los datos (Si el token es falso o expiró, esto dará un error y saltará al catch)
                Claims claims = jwtAdaptador.obtenerClaims(token);

                // 4. Le decimos a Spring Security: "Tranquilo, yo lo conozco, déjalo pasar"
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null, null);
                SecurityContextHolder.getContext().setAuthentication(auth);

                // 5. ¡TRUCO DE CAPO! Guardamos el ID del usuario en la petición para que el Controlador lo tenga a mano
                request.setAttribute("usuarioId", claims.get("id", Long.class));

            } catch (Exception e) {
                // Token inválido, no hacemos nada y dejamos que Spring Security lo bloquee
                SecurityContextHolder.clearContext();
            }
        }

        // Continuamos con el viaje de la petición
        chain.doFilter(request, response);
    }
}