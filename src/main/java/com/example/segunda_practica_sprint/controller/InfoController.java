// Declaración del paquete donde residen los controladores REST
package com.example.segunda_practica_sprint.controller;

// Importación de la clase de propiedades de configuración tipadas
import com.example.segunda_practica_sprint.config.AppInfoProperties;
// Importación del DTO record de respuesta
import com.example.segunda_practica_sprint.dto.AppInfoResponse;
// Importaciones de anotaciones de Spring Web MVC
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST que expone información descriptiva de la aplicación.
 * Sigue las mejores prácticas de arquitectura inyectando la configuración por constructor,
 * prescindiendo totalmente de @Value para garantizar modularidad y testabilidad.
 */
@RestController // Marca la clase como un controlador de Spring donde cada método devuelve directamente el cuerpo de respuesta (JSON)
@RequestMapping("/api") // Define el prefijo de ruta base '/api' para todos los endpoints contenidos en este controlador
public class InfoController {

    // Variable inmutable que almacena la referencia a las propiedades cargadas
    private final AppInfoProperties appInfoProperties;

    /**
     * Constructor para la inyección de dependencias de Spring (Constructor Injection).
     * @param appInfoProperties bean de propiedades provisto automáticamente por el contenedor de Spring.
     */
    public InfoController(AppInfoProperties appInfoProperties) {
        this.appInfoProperties = appInfoProperties; // Asignación de la dependencia inyectada
    }

    /**
     * Endpoint HTTP GET mapeado a la ruta '/api/info'.
     * Construye y devuelve el DTO AppInfoResponse que Spring serializa automáticamente a formato JSON.
     * @return objeto AppInfoResponse con los valores actuales de configuración.
     */
    @GetMapping("/info") // Mapea peticiones HTTP GET sobre la ruta '/api/info' a este método
    public AppInfoResponse getInfo() {
        // Retorna una nueva instancia del DTO con los valores obtenidos de AppInfoProperties
        return new AppInfoResponse(
                appInfoProperties.getName(),        // Nombre de la app
                appInfoProperties.getDescription(), // Descripción
                appInfoProperties.getVersion(),     // Versión desde el pom.xml
                appInfoProperties.getDeveloper(),   // Desarrollador
                appInfoProperties.getEmail(),       // Correo electrónico
                appInfoProperties.getRole(),        // Rol
                appInfoProperties.getEnvironment()  // Entorno (dev, prod, etc.)
        );
    }
}
