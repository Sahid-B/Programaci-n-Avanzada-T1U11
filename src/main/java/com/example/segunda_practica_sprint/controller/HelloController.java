// Declaración del paquete de controladores
package com.example.segunda_practica_sprint.controller;

// Importaciones de anotaciones de Spring Web MVC
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador que saluda en múltiples idiomas según los parámetros recibidos por URL.
 */
@RestController // Expone las respuestas directamente en el cuerpo HTTP
public class HelloController {

    // 1. Manejador cuando NO se envía el parámetro 'idioma' (params = "!idioma")
    @GetMapping(value = "/home", params = "!idioma") // Se ejecuta si en la petición falta el parámetro idioma
    public String homeDefault(@RequestParam(value = "nombre", required = false, defaultValue = "Sahid") String nombre) {
        // Retorna saludo en español usando el nombre recibido o el valor por defecto
        return String.format("<h1>Hola %s!</h1>", nombre);
    }

    // 2. Manejador para idioma Español (params = "idioma=ES")
    @GetMapping(value = "/home", params = "idioma=ES") // Se activa si idioma es exactamente ES
    public String homeEs(@RequestParam(value = "nombre", required = false, defaultValue = "Sahid") String nombre) {
        // Retorna saludo formal en español
        return String.format("<h1>Hola %s!</h1>", nombre);
    }

    // 3. Manejador para idioma Inglés (params = "idioma=EN")
    @GetMapping(value = "/home", params = "idioma=EN") // Se activa si idioma es exactamente EN
    public String homeEn(@RequestParam(value = "nombre", required = false, defaultValue = "Sahid") String nombre) {
        // Retorna saludo en inglés
        return String.format("<h1>Hello %s!</h1>", nombre);
    }

    // 4. Manejador para idioma Portugués (params = "idioma=PO")
    @GetMapping(value = "/home", params = "idioma=PO") // Se activa si idioma es exactamente PO
    public String homePo(@RequestParam(value = "nombre", required = false, defaultValue = "Sahid") String nombre) {
        // Retorna saludo en portugués
        return String.format("<h1>Olá %s!</h1>", nombre);
    }

    // 5. Manejador para idioma Árabe egipcio (params = "idioma=AR")
    @GetMapping(value = "/home", params = "idioma=AR") // Se activa si idioma es exactamente AR
    public String homeAr(@RequestParam(value = "nombre", required = false, defaultValue = "شافه") String nombre) {
        // Retorna saludo en árabe
        return String.format("<h1>أزيك %s!</h1>", nombre);
    }

    // 6. Manejador para idioma Ruso (params = "idioma=RU")
    @GetMapping(value = "/home", params = "idioma=RU") // Se activa si idioma es exactamente RU
    public String homeRu(@RequestParam(value = "nombre", required = false, defaultValue = "Сахид") String nombre) {
        // Retorna saludo en ruso
        return String.format("<h1>привет %s!</h1>", nombre);
    }
}
