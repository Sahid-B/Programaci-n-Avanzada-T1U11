package com.example.segunda_practica_sprint.controller;

import com.example.segunda_practica_sprint.config.AppInfoProperties;
import com.example.segunda_practica_sprint.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas unitarias para InfoController utilizando @WebMvcTest + MockMvc.
 * - @Import(SecurityConfig.class): Aplica la configuración de seguridad para validar el acceso público a /api/info.
 * - @EnableConfigurationProperties(AppInfoProperties.class): Carga la clase de propiedades tipadas en el contexto del test.
 * - @TestPropertySource: Inyecta los valores de prueba que cumplen con las validaciones @NotBlank y @Email.
 */
@WebMvcTest(InfoController.class)
@Import(SecurityConfig.class)
@EnableConfigurationProperties(AppInfoProperties.class)
@TestPropertySource(properties = {
        "app.info.name=Segunda_Practica_Sprint",
        "app.info.description=Segunda Practica de Spring Boot",
        "app.info.version=0.0.1-SNAPSHOT",
        "app.info.developer=Sahid",
        "app.info.email=sahid@espe.edu.ec",
        "app.info.role=Developer",
        "app.info.environment=test"
})
public class InfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // =========================================================================
    // 1. Validar Código HTTP 200
    // =========================================================================
    @Test
    @DisplayName("1. Validar Código HTTP 200 en GET /api/info")
    void testCodigoHttp200() throws Exception {
        mockMvc.perform(get("/api/info"))
                .andExpect(status().isOk());
    }

    // =========================================================================
    // 2. Validar Estructura JSON (al menos 3 campos)
    // =========================================================================
    @Test
    @DisplayName("2. Validar Estructura JSON (al menos 3 campos con jsonPath)")
    void testEstructuraJson() throws Exception {
        mockMvc.perform(get("/api/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Segunda_Practica_Sprint"))
                .andExpect(jsonPath("$.version").value("0.0.1-SNAPSHOT"))
                .andExpect(jsonPath("$.developer").value("Sahid"))
                .andExpect(jsonPath("$.role").value("Developer"))
                .andExpect(jsonPath("$.environment").value("test"));
    }

    // =========================================================================
    // 3. Validar que el email tenga formato válido
    // =========================================================================
    @Test
    @DisplayName("3. Validar que el email del developer tenga formato válido (Regex)")
    void testEmailFormatoValido() throws Exception {
        mockMvc.perform(get("/api/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(matchesPattern("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")));
    }
}
