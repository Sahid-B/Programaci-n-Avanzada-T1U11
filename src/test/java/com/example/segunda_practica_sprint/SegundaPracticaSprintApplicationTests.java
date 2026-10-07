// Declaración del paquete donde se ubica la prueba de integración
package com.example.segunda_practica_sprint;

// Importaciones de JUnit 5 y de soporte de pruebas para Spring Boot
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Prueba de integración del ciclo de vida y arranque del contexto completo de Spring Boot.
 */
@SpringBootTest // Levanta todo el ApplicationContext de la aplicación para verificar que la configuración sea coherente
class SegundaPracticaSprintApplicationTests {

    /**
     * Prueba básica de sanidad ("Smoke Test") que valida que todos los beans,
     * configuraciones de seguridad, propiedades y DataSource se inicialicen sin excepciones.
     */
    @Test // Anotación de JUnit 5 que define un caso de prueba ejecutable
    void contextLoads() {
        // Si el contexto de Spring no puede inicializar algún bean (ej. por falta de dependencias), este método falla.
        // Al estar vacío, pasar en verde confirma que el contexto cargó al 100% exitosamente.
    }

}
