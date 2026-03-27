package daw.gestiongastos.shared;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFiltro jwtFiltro;

    public SecurityConfig(JwtFiltro jwtFiltro) {
        this.jwtFiltro = jwtFiltro;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Configuración CORS y desactivación de CSRF (esencial para APIs REST con JWT)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())

                // 2. Reglas de Autorización
                .authorizeHttpRequests(auth -> auth
                        // Permitimos explícitamente las peticiones OPTIONS (CORS preflight)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Rutas públicas (HTML, CSS, JS)
                        .requestMatchers(
                                "/",
                                "/*.html",
                                "/css/**",
                                "/js/**",
                                "/img/**",
                                "/assets/**"
                        ).permitAll()

                        // Endpoints de registro y login son públicos
                        .requestMatchers("/api/usuarios/registro", "/api/usuarios/login").permitAll()

                        // ¡CUALQUIER otra petición debe estar autenticada!
                        .anyRequest().permitAll()
                )

                // 3. Configuración CLAVE para JWT: Le decimos a Spring que no guarde sesiones.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 4. Inyectamos nuestro filtro personalizado ANTES del filtro estándar de Spring
                .addFilterBefore(jwtFiltro, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Configuración global de CORS para permitir todos los métodos desde cualquier origen (localhost)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")); // Aseguramos todo el CRUD
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type")); // Headers esenciales

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}