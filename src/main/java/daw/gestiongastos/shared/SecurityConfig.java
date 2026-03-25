package daw.gestiongastos.shared;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // ¡AQUÍ ESTÁ LA MAGIA! Añadimos las rutas de nuestros archivos HTML, CSS y JS
                        .requestMatchers(
                                "/api/usuarios/registro",
                                "/api/usuarios/login",
                                "/",                  // Permite la raíz
                                "/index.html",        // Permite la vista principal
                                "/login.html",        // Permite la vista de login
                                "/registro.html",     // Permite la vista de registro
                                "/css/**",            // Permite cualquier archivo dentro de la carpeta css
                                "/js/**"              // Permite cualquier archivo dentro de la carpeta js
                        ).permitAll()
                        // Cualquier otra cosa (como guardar un gasto en el futuro) pedirá Token
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}