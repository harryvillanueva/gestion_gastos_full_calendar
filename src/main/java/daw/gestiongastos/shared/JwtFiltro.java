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
import java.util.Collections;

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

                // 3. Extraemos los datos
                Claims claims = jwtAdaptador.obtenerClaims(token);

                // 🚀 CORRECCIÓN 1: En lugar de 'null', le pasamos una lista vacía con Collections.emptyList()
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        claims.getSubject(),
                        null,
                        Collections.emptyList()
                );
                SecurityContextHolder.getContext().setAuthentication(auth);

                // 🚀 CORRECCIÓN 2: Extraemos el número de forma segura, sea Integer o Long, y lo convertimos a Long.
                Number idNumber = (Number) claims.get("id");
                request.setAttribute("usuarioId", idNumber.longValue());

            } catch (Exception e) {
                // Si vuelve a fallar por lo que sea, ahora sí imprimiremos el error en tu consola para verlo
                System.out.println("Error procesando el token: " + e.getMessage());
                SecurityContextHolder.clearContext();
            }
        }

        // Continuamos con el viaje de la petición
        chain.doFilter(request, response);
    }
}