// Declaración del paquete base de la aplicación Spring Boot
package com.example.segunda_practica_sprint;

// Importaciones de clases fundamentales para el ciclo de vida de Spring Boot
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de inicio (punto de entrada) de la aplicación Spring Boot.
 * Combina @Configuration, @EnableAutoConfiguration y @ComponentScan.
 */
@SpringBootApplication // Activa la autoconfiguración de Spring Boot y el escaneo de componentes en este paquete y subpaquetes
public class SegundaPracticaSprintApplication {

    /**
     * Método principal estándar de Java que inicia la ejecución del programa.
     * @param args argumentos pasados por línea de comandos.
     */
    public static void main(String[] args) {
        // Arranca el contenedor de Spring Boot (ApplicationContext), levanta el servidor web embebido e inicializa los beans
        SpringApplication.run(SegundaPracticaSprintApplication.class, args);
    }
}