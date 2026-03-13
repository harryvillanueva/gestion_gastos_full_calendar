package daw.gestiongastos.shared;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitamos CSRF porque en una API REST que no usa cookies de sesión no es necesario (usaremos tokens más adelante)
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Permitimos el paso libre a nuestra ruta de registro
                        .requestMatchers("/api/usuarios/registro").permitAll()
                        // Cualquier otra petición requerirá autenticación
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
