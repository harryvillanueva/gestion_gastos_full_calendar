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

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            try {
                String token = header.substring(7);
                Claims claims = jwtAdaptador.obtenerClaims(token);

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        claims.getSubject(), null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(auth);

                Number idNumber = (Number) claims.get("id");
                request.setAttribute("usuarioId", idNumber.longValue());

                request.setAttribute("rol", claims.get("rol", String.class));

            } catch (Exception e) {
                System.out.println("Error procesando el token: " + e.getMessage());
                SecurityContextHolder.clearContext();
            }
        }

        chain.doFilter(request, response);
    }
}