// Declaración del paquete donde reside la configuración de seguridad
package com.example.segunda_practica_sprint.config;

// Importaciones necesarias de Spring Framework y Spring Security
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Clase de configuración central de seguridad web para Spring Boot 4.
 * Define la cadena de filtros de seguridad (SecurityFilterChain) que intercepta todas las peticiones HTTP.
 */
@Configuration // Indica a Spring que esta clase contiene definiciones de Beans de configuración
@EnableWebSecurity // Habilita el soporte de seguridad web de Spring Security en la aplicación
public class SecurityConfig {

    /**
     * Bean que construye y personaliza la cadena de filtros de seguridad HTTP.
     * @param http objeto HttpSecurity para configurar la seguridad a nivel de rutas web.
     * @return SecurityFilterChain ensamblado y aplicado al contexto de Spring.
     * @throws Exception en caso de error durante la configuración de seguridad.
     */
    @Bean // Registra el SecurityFilterChain devuelto como un Bean gestionado en el ApplicationContext
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilita la protección CSRF (Cross-Site Request Forgery) porque esta es una API REST sin estado (stateless)
                .csrf(csrf -> csrf.disable())

                // Configura las reglas de autorización para cada petición HTTP entrante
                .authorizeHttpRequests(auth -> auth
                        // Permite acceso libre y anónimo (sin autenticación) a /api/info y a la comprobación de salud /actuator/health
                        .requestMatchers("/api/info", "/actuator/health").permitAll()
                        // Exige que cualquier otra petición a cualquier ruta diferente requiera estar autenticada
                        .anyRequest().authenticated()
                )

                // Habilita el mecanismo de autenticación estándar HTTP Basic (usuario y contraseña en cabecera)
                .httpBasic(Customizer.withDefaults());

        // Construye y retorna la cadena de filtros configurada
        return http.build();
    }
}
