// Declaración del paquete donde se ubican las configuraciones
package com.example.segunda_practica_sprint.config;

// Importaciones de validación estándar de Jakarta Bean Validation
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
// Importaciones del framework Spring Boot para enlace tipado de propiedades y validación
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * Clase que enlaza y valida las propiedades del archivo application.properties con prefijo 'app.info'.
 */
@Component // Registra esta clase como un Bean gestionado dentro del contenedor de dependencias de Spring
@ConfigurationProperties(prefix = "app.info") // Enlaza automáticamente las propiedades con prefijo 'app.info' a los atributos de esta clase
@Validated // Activa la validación de Bean Validation (JSR-380) en el momento del arranque (fail-fast)
public class AppInfoProperties {

    // 1. Nombre de la aplicación (obligatorio)
    @NotBlank(message = "El nombre de la aplicación no puede estar en blanco") // Exige que la cadena no sea nula ni contenga solo espacios
    private String name;

    // 2. Descripción funcional (obligatoria)
    @NotBlank(message = "La descripción no puede estar en blanco") // Valida que la descripción esté presente
    private String description;

    // 3. Versión del proyecto (obligatoria, proveniente del pom.xml)
    @NotBlank(message = "La versión no puede estar en blanco") // Valida que la versión no esté vacía
    private String version;

    // 4. Nombre del desarrollador (obligatorio)
    @NotBlank(message = "El nombre del desarrollador no puede estar en blanco") // Valida que el nombre del desarrollador exista
    private String developer;

    // 5. Correo del desarrollador (obligatorio y con formato válido)
    @NotBlank(message = "El email no puede estar en blanco") // No permite cadena vacía o nula
    @Email(message = "El email debe ser una dirección de correo válida") // Exige una estructura sintáctica de email válida (ej. usuario@dominio.com)
    private String email;

    // 6. Rol dentro del proyecto (obligatorio)
    @NotBlank(message = "El rol no puede estar en blanco") // Valida que el rol esté definido
    private String role;

    // 7. Entorno de ejecución (obligatorio, ej. dev, prod)
    @NotBlank(message = "El entorno no puede estar en blanco") // Valida que el entorno esté configurado
    private String environment;

    // Métodos Getter y Setter requeridos por Spring Boot para inyectar los valores desde los archivos .properties

    // Getter para name
    public String getName() {
        return name;
    }

    // Setter para name (usado por Spring Boot al enlazar la propiedad)
    public void setName(String name) {
        this.name = name;
    }

    // Getter para description
    public String getDescription() {
        return description;
    }

    // Setter para description
    public void setDescription(String description) {
        this.description = description;
    }

    // Getter para version
    public String getVersion() {
        return version;
    }

    // Setter para version
    public void setVersion(String version) {
        this.version = version;
    }

    // Getter para developer
    public String getDeveloper() {
        return developer;
    }

    // Setter para developer
    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    // Getter para email
    public String getEmail() {
        return email;
    }

    // Setter para email
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter para role
    public String getRole() {
        return role;
    }

    // Setter para role
    public void setRole(String role) {
        this.role = role;
    }

    // Getter para environment
    public String getEnvironment() {
        return environment;
    }

    // Setter para environment
    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}
